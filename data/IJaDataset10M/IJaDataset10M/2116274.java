package ti.plato.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oscript.data.OJavaException;
import oscript.exceptions.PackagedScriptObjectException;
import oscript.parser.ParseException;
import ti.mcore.u.FileUtil;
import ti.mcore.u.StringUtil;
import ti.mcore.u.log.PlatoLogger;
import ti.plato.ui.u.LinkableTextContents;

/**
 * Links to OScript files utilities.
 *
 * @author alex.k@ti.com
 */
public class OScriptLinkUtil {

    private static final PlatoLogger LOGGER = PlatoLogger.getLogger(OScriptLinkUtil.class);

    private static final Pattern PATTERN_TMP_OS_FILE = Pattern.compile("/.tmp/.*/string-input\\.os");

    private OScriptLinkUtil() {
    }

    /**
	 * Assumption: there is only one valid .os path per line, and it is first.
	 * 
	 * @param lineInfo
	 * @return String[] of valid .os paths. <code>null</code> if no valid .os
	 *         paths found. Return value is guaranteed to not contain
	 *         <code>null</code> elements.
	 */
    public static String[] getValidOSPaths(String lineInfo) {
        if (lineInfo == null) {
            return null;
        }
        String[] fragments = lineInfo.split("[(|)|'|\"|\\[|\\]]");
        if (fragments == null) {
            return null;
        }
        ArrayList<String> paths = new ArrayList<String>();
        for (int i = 0; i < fragments.length; i++) {
            String path = fragments[i];
            if (path == null || !path.contains(".os")) {
                continue;
            }
            path = path.split("\\.os", 2)[0] + ".os";
            path = getValidOSPathWithSpaces(path);
            if (path == null) {
                continue;
            }
            paths.add(path);
        }
        if (paths.size() == 0) {
            return null;
        }
        return paths.toArray(StringUtil.ZERO_STR_ARRAY);
    }

    private static String getValidOSPathWithSpaces(String path) {
        if (path == null || FileUtil.exists(path)) {
            return path;
        }
        String[] fragments = path.split(" ", 2);
        String newRet = null;
        if (fragments.length == 2) {
            newRet = fragments[1];
        } else {
            newRet = fragments[0];
        }
        if (newRet == null) {
            return null;
        }
        if (newRet.startsWith("@Workspace")) {
            return path;
        }
        if (newRet.equals(path) && !FileUtil.exists(path)) {
            return null;
        }
        return getValidOSPathWithSpaces(newRet);
    }

    /**
	 * @param frameStr
	 * @return path to /.tmp/.../string-input.os, or null if not found.
	 *
	 * @author alex.k@ti.com
	 */
    public static String getTmpOsFile(String frameStr) {
        Matcher m = PATTERN_TMP_OS_FILE.matcher(frameStr);
        if (m.find()) {
            String tmpFile = m.group();
            return tmpFile;
        }
        return null;
    }

    /**
	 * @param args -
	 *            must be String[2] with no <code>null</code> Strings.
	 *            <code>args[0]</code> is the path string, and args[1] is the
	 *            original String containing path.
	 * 
	 * @author alex.k@ti.com
	 */
    public static void openInEditor(String[] args) {
        if (args == null) {
            LOGGER.logError("arguments == null", true);
            return;
        }
        if (args.length != 2) {
            StringBuffer sb = new StringBuffer("arguments.length != 2; {");
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]);
                sb.append(", ");
            }
            sb.append("}");
            LOGGER.logError(sb.toString(), true);
            return;
        }
        if (args[0].startsWith("@Workspace")) {
            String wsPath = WorkspaceManagement.getDefault().getCurrentWorkspaceDirectory();
            args[0] = args[0].replace("@Workspace", wsPath);
            args[1] = args[1].replace("@Workspace", wsPath);
        }
        String path = args[0];
        if (path == null) {
            LOGGER.logError("path == null", true);
            return;
        }
        String frameStr = args[1];
        if (frameStr == null) {
            LOGGER.logError("frameStr == null", true);
            return;
        }
        OScriptPathHyperlink hyper;
        try {
            hyper = new OScriptPathHyperlink(path, frameStr, 0, frameStr.length());
        } catch (InvalidOScriptPathInfoException e) {
            LOGGER.logError(e);
            return;
        }
        hyper.linkActivated();
    }

    public static boolean hasOScriptFileReference(String frameStr) {
        if (frameStr == null) {
            return false;
        }
        if (frameStr.contains(".os")) {
            return true;
        }
        return false;
    }

    /**
	 * 
	 * @param description     the exception message with hyperlinks is appended to this contents
	 * @param e               the exception
	 * @param formulaLink     if not null, this link is used to replace the "/.tmp/.../string-input.os" frame in the exception
	 */
    public static void generateExceptionLinks(LinkableTextContents description, Throwable e, LinkableTextContents formulaLink) {
        if (e instanceof PackagedScriptObjectException) {
            PackagedScriptObjectException se = (PackagedScriptObjectException) e;
            if (se.val instanceof OJavaException) {
                OJavaException oje = (OJavaException) se.val;
                if (oje.castToJavaObject() instanceof ParseException) {
                    e = (ParseException) oje.castToJavaObject();
                }
            }
        }
        if ((e instanceof PackagedScriptObjectException) || (e instanceof ParseException)) {
            String msg = e.getMessage();
            String wsPath = WorkspaceManagement.getDefault().getCurrentWorkspaceDirectory().replace('\\', '/');
            if (!wsPath.startsWith("/")) {
                wsPath = "/" + wsPath;
            }
            msg = msg.replace(wsPath, "@Workspace");
            String[] frames = getFrames(msg);
            for (int i = 0; i < frames.length; i++) {
                if (i > 0) description.appendText("\n");
                description.append(insertLink(frames[i], formulaLink));
            }
        } else {
            description.appendText(e.getMessage());
        }
    }

    private static String[] getFrames(String msg) {
        if (msg == null) {
            return StringUtil.ZERO_STR_ARRAY;
        }
        return msg.split("\n");
    }

    public static LinkableTextContents insertLink(String frameStr, LinkableTextContents formulaLink) {
        LinkableTextContents text = new LinkableTextContents();
        if (frameStr == null) {
            return text;
        }
        if (!OScriptLinkUtil.hasOScriptFileReference(frameStr)) {
            return text.appendText(frameStr);
        }
        String[] paths = OScriptLinkUtil.getValidOSPaths(frameStr);
        if (paths == null) {
            String tmpFile = OScriptLinkUtil.getTmpOsFile(frameStr);
            if ((tmpFile == null) || (formulaLink == null)) {
                return text.appendText(frameStr);
            }
            return text.appendText("  at ").append(formulaLink);
        }
        if (paths.length > 1) {
            LOGGER.logWarning("paths.length > 1", true);
        }
        String pathStr = paths[0];
        int idx = frameStr.indexOf(pathStr);
        if (idx < 0) {
            LOGGER.logError("bad idx!");
            return text.appendText(frameStr);
        }
        return text.appendText(frameStr.substring(0, idx)).appendLink(pathStr, LinkableTextContents.LM_EDITOR, new String[] { pathStr, frameStr }).appendText(frameStr.substring(idx + pathStr.length()));
    }
}
