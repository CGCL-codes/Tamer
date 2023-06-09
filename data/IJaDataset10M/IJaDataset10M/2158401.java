package org.geometerplus.android.fbreader;

import java.util.*;
import android.app.*;
import android.content.*;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.options.ZLStringOption;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.core.xml.ZLXMLReaderAdapter;
import org.geometerplus.zlibrary.core.xml.ZLStringMap;
import org.geometerplus.zlibrary.text.view.ZLTextWordRegion;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;
import org.geometerplus.android.util.UIUtil;
import org.geometerplus.android.util.PackageUtil;

public abstract class DictionaryUtil {

    private static LinkedHashMap<PackageInfo, Boolean> ourDictionaryInfos = new LinkedHashMap<PackageInfo, Boolean>();

    private static ZLStringOption ourDictionaryOption;

    private static class InfoReader extends ZLXMLReaderAdapter {

        @Override
        public boolean dontCacheAttributeValues() {
            return true;
        }

        @Override
        public boolean startElementHandler(String tag, ZLStringMap attributes) {
            if ("dictionary".equals(tag)) {
                final String id = attributes.getValue("id");
                final String title = attributes.getValue("title");
                ourDictionaryInfos.put(new PackageInfo(id, attributes.getValue("package"), attributes.getValue("class"), title != null ? title : id, attributes.getValue("action"), attributes.getValue("dataKey"), attributes.getValue("pattern")), !"always".equals(attributes.getValue("list")));
            }
            return false;
        }
    }

    private interface ColorDict3 {

        String ACTION = "colordict.intent.action.SEARCH";

        String QUERY = "EXTRA_QUERY";

        String HEIGHT = "EXTRA_HEIGHT";

        String WIDTH = "EXTRA_WIDTH";

        String GRAVITY = "EXTRA_GRAVITY";

        String MARGIN_LEFT = "EXTRA_MARGIN_LEFT";

        String MARGIN_TOP = "EXTRA_MARGIN_TOP";

        String MARGIN_BOTTOM = "EXTRA_MARGIN_BOTTOM";

        String MARGIN_RIGHT = "EXTRA_MARGIN_RIGHT";

        String FULLSCREEN = "EXTRA_FULLSCREEN";
    }

    private static Map<PackageInfo, Boolean> infos() {
        if (ourDictionaryInfos.isEmpty()) {
            new InfoReader().read(ZLFile.createFileByPath("dictionaries.xml"));
        }
        return ourDictionaryInfos;
    }

    public static List<PackageInfo> dictionaryInfos(Context context) {
        final LinkedList<PackageInfo> list = new LinkedList<PackageInfo>();
        for (Map.Entry<PackageInfo, Boolean> entry : infos().entrySet()) {
            final PackageInfo info = entry.getKey();
            if (!entry.getValue() || PackageUtil.canBeStarted(context, getDictionaryIntent(info, "test"))) {
                list.add(info);
            }
        }
        return list;
    }

    private static PackageInfo firstInfo() {
        for (Map.Entry<PackageInfo, Boolean> entry : infos().entrySet()) {
            if (!entry.getValue()) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("There are no available dictionary infos");
    }

    public static ZLStringOption dictionaryOption() {
        if (ourDictionaryOption == null) {
            ourDictionaryOption = new ZLStringOption("Dictionary", "Id", firstInfo().Id);
        }
        return ourDictionaryOption;
    }

    private static PackageInfo getCurrentDictionaryInfo() {
        final String id = dictionaryOption().getValue();
        for (PackageInfo info : infos().keySet()) {
            if (info.Id.equals(id)) {
                return info;
            }
        }
        return firstInfo();
    }

    private static Intent getDictionaryIntent(String text) {
        return getDictionaryIntent(getCurrentDictionaryInfo(), text);
    }

    public static Intent getDictionaryIntent(PackageInfo dictionaryInfo, String text) {
        final Intent intent = new Intent(dictionaryInfo.IntentAction);
        if (dictionaryInfo.PackageName != null) {
            String cls = dictionaryInfo.ClassName;
            if (cls != null && cls.startsWith(".")) {
                cls = dictionaryInfo.PackageName + cls;
            }
            intent.setComponent(new ComponentName(dictionaryInfo.PackageName, cls));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        text = dictionaryInfo.IntentDataPattern.replace("%s", text);
        if (dictionaryInfo.IntentKey != null) {
            return intent.putExtra(dictionaryInfo.IntentKey, text);
        } else {
            return intent.setData(Uri.parse(text));
        }
    }

    public static void openWordInDictionary(Activity activity, ZLTextWordRegion region) {
        String text = region.Word.toString();
        int start = 0;
        int end = text.length();
        for (; start < end && !Character.isLetterOrDigit(text.charAt(start)); ++start) ;
        for (; start < end && !Character.isLetterOrDigit(text.charAt(end - 1)); --end) ;
        if (start == end) {
            return;
        }
        final PackageInfo info = getCurrentDictionaryInfo();
        final Intent intent = getDictionaryIntent(info, text.substring(start, end));
        try {
            if ("ColorDict".equals(info.Id)) {
                final DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                final int screenHeight = metrics.heightPixels;
                final int topSpace = region.getTop();
                final int bottomSpace = metrics.heightPixels - region.getBottom();
                final boolean showAtBottom = bottomSpace >= topSpace;
                final int space = (showAtBottom ? bottomSpace : topSpace) - 20;
                final int maxHeight = Math.min(400, screenHeight * 2 / 3);
                final int minHeight = Math.min(200, screenHeight * 2 / 3);
                intent.putExtra(ColorDict3.HEIGHT, Math.max(minHeight, Math.min(maxHeight, space)));
                intent.putExtra(ColorDict3.GRAVITY, showAtBottom ? Gravity.BOTTOM : Gravity.TOP);
                final ZLAndroidApplication application = ZLAndroidApplication.Instance();
                intent.putExtra(ColorDict3.FULLSCREEN, !application.ShowStatusBarOption.getValue());
            }
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            DictionaryUtil.installDictionaryIfNotInstalled(activity);
        }
    }

    public static void installDictionaryIfNotInstalled(final Activity activity) {
        if (PackageUtil.canBeStarted(activity, getDictionaryIntent("test"))) {
            return;
        }
        final PackageInfo dictionaryInfo = getCurrentDictionaryInfo();
        final ZLResource dialogResource = ZLResource.resource("dialog");
        final ZLResource buttonResource = dialogResource.getResource("button");
        final ZLResource installResource = dialogResource.getResource("installDictionary");
        new AlertDialog.Builder(activity).setTitle(installResource.getResource("title").getValue()).setMessage(installResource.getResource("message").getValue().replace("%s", dictionaryInfo.Title)).setIcon(0).setPositiveButton(buttonResource.getResource("install").getValue(), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                installDictionary(activity, dictionaryInfo);
            }
        }).setNegativeButton(buttonResource.getResource("skip").getValue(), null).create().show();
    }

    private static void installDictionary(Activity activity, PackageInfo dictionaryInfo) {
        if (!PackageUtil.installFromMarket(activity, dictionaryInfo.PackageName)) {
            UIUtil.showErrorMessage(activity, "cannotRunAndroidMarket", dictionaryInfo.Title);
        }
    }
}
