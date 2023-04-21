package com.yingyonghui.market;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.yingyonghui.market.install.InstallAppProgress;
import com.yingyonghui.market.install.UninstallAppProgress;
import com.yingyonghui.market.model.Asset;
import com.yingyonghui.market.online.DownloadService;
import com.yingyonghui.market.online.MarketService;
import com.yingyonghui.market.online.Request;
import com.yingyonghui.market.util.GlobalUtil;
import com.yingyonghui.market.util.PackageInstallInfo;
import dalvik.annotation.Signature;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Signature({"Landroid/widget/ArrayAdapter", "<", "Lcom/yingyonghui/market/model/Asset;", ">;"})
public class FilteredAppListAdapter extends ArrayAdapter
{
  private static final int DAY = 86400000;
  private static final int HOUR = 3600000;
  private static final int MINUTE = 60000;
  private static final long MONTH = 2592000000L;
  private static final int SECOND = 1000;
  private static final int WEEK = 604800000;

  @Signature({"Ljava/util/ArrayList", "<", "Ljava/lang/Integer;", ">;"})
  private ArrayList downloadArray;
  private NumberFormat instance;
  private View.OnClickListener listener;
  private Asset mAsset;
  private Context mContext;
  private Handler mDownloadStatusHandler;
  private Handler mHandler;
  private int mItemType;
  private LayoutInflater mLayoutInflater;
  private Drawable thumb = null;

  @Signature({"(", "Landroid/content/Context;", "Ljava/util/ArrayList", "<", "Lcom/yingyonghui/market/model/Asset;", ">;I)V"})
  public FilteredAppListAdapter(Context paramContext, ArrayList paramArrayList, int paramInt)
  {
    super(paramContext, 0, paramArrayList);
    ArrayList localArrayList = new ArrayList();
    this.downloadArray = localArrayList;
    this.mAsset = null;
    NumberFormat localNumberFormat = NumberFormat.getInstance();
    this.instance = localNumberFormat;
    FilteredAppListAdapter.1 local1 = new FilteredAppListAdapter.1(this);
    this.listener = local1;
    LayoutInflater localLayoutInflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
    this.mLayoutInflater = localLayoutInflater;
    this.mContext = paramContext;
    this.mItemType = paramInt;
    Handler localHandler1 = WifiDownloadManager.initHandlerInUIThread(this.mContext);
    this.mHandler = localHandler1;
    if ((this.mContext instanceof FilteredAppListActivity))
    {
      Handler localHandler2 = ((FilteredAppListActivity)paramContext).getDownloadStatusHandler();
      this.mDownloadStatusHandler = localHandler2;
    }
    while (true)
    {
      return;
      if ((this.mContext instanceof SearchAssetListActivity))
      {
        Handler localHandler3 = ((SearchAssetListActivity)paramContext).getDownloadStatusHandler();
        this.mDownloadStatusHandler = localHandler3;
        continue;
      }
      if ((this.mContext instanceof CommonAppListActivity))
      {
        Handler localHandler4 = ((CommonAppListActivity)paramContext).getDownloadStatusHandler();
        this.mDownloadStatusHandler = localHandler4;
        continue;
      }
      if (!(this.mContext instanceof UpdateAppListActivity))
        continue;
      Handler localHandler5 = ((UpdateAppListActivity)paramContext).getDownloadStatusHandler();
      this.mDownloadStatusHandler = localHandler5;
    }
  }

  private void addDownloadAndInstallRequest()
  {
    Request localRequest = new Request(0L, 65541);
    int i = this.mAsset.size;
    String str1 = this.mAsset.pkgName;
    String str2 = this.mAsset.title;
    Object[] arrayOfObject = new Object[4];
    Integer localInteger1 = Integer.valueOf(this.mAsset._id);
    arrayOfObject[0] = localInteger1;
    Integer localInteger2 = Integer.valueOf(i);
    arrayOfObject[1] = localInteger2;
    arrayOfObject[2] = str1;
    arrayOfObject[3] = str2;
    localRequest.setData(arrayOfObject);
    Handler localHandler = this.mHandler;
    localRequest.addWifiObserver(localHandler);
    MarketService.getServiceInstance(this.mContext).getAppContentStream(localRequest);
    ArrayList localArrayList = this.downloadArray;
    Integer localInteger3 = Integer.valueOf(this.mAsset._id);
    localArrayList.add(localInteger3);
  }

  private boolean checkAppDownloading(int paramInt)
  {
    return DownloadService.getInstance(this.mContext).isTaskRunning(paramInt);
  }

  private File getFile(Asset paramAsset)
  {
    return FileManager.getFile(this.mContext, paramAsset);
  }

  private String getFromPage()
  {
    String str;
    if ((this.mContext instanceof FilteredAppListActivity))
      str = ((FilteredAppListActivity)this.mContext).getPage();
    while (true)
    {
      return str;
      if ((this.mContext instanceof SearchAssetListActivity))
      {
        str = "SearchAsset";
        continue;
      }
      if ((this.mContext instanceof CommonAppListActivity))
      {
        str = "New";
        continue;
      }
      if ((this.mContext instanceof UpdateAppListActivity))
      {
        str = "Update";
        continue;
      }
      str = "";
    }
  }

  private String getPriceString(Asset paramAsset)
  {
    NumberFormat localNumberFormat;
    double d;
    if (paramAsset.price != 0.0F)
    {
      localNumberFormat = NumberFormat.getInstance();
      localNumberFormat.setMaximumIntegerDigits(5);
      d = paramAsset.price;
    }
    for (String str = localNumberFormat.format(d); ; str = this.mContext.getString(2131296356))
      return str;
  }

  private String getSize(int paramInt)
  {
    String str1;
    if (paramInt > 1048576)
    {
      this.instance.setMaximumFractionDigits(2);
      NumberFormat localNumberFormat1 = this.instance;
      double d = paramInt / 1048576.0F;
      str1 = String.valueOf(localNumberFormat1.format(d));
    }
    String str4;
    for (String str2 = str1 + "M"; ; str2 = str4 + "K")
    {
      String str3 = String.valueOf(str2);
      return str3;
      this.instance.setMaximumFractionDigits(0);
      NumberFormat localNumberFormat2 = this.instance;
      long l = paramInt / 1024;
      str4 = String.valueOf(localNumberFormat2.format(l));
    }
  }

  private String getUpdateTime(String paramString)
  {
    try
    {
      l1 = Long.parseLong(paramString);
      long l2 = System.currentTimeMillis();
      long l3 = l2 % 86400000L;
      l4 = l2 - l3 - l1;
      if (l4 < 0L)
      {
        str1 = this.mContext.getResources().getString(2131296426);
        str2 = str1;
        return str2;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        long l1;
        long l4;
        String str2 = this.mContext.getResources().getString(2131296425);
        continue;
        if (l4 < 86400000L)
        {
          str1 = this.mContext.getResources().getString(2131296427);
          continue;
        }
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("MM/dd");
        Long localLong = Long.valueOf(l1);
        String str1 = localSimpleDateFormat.format(localLong);
      }
    }
  }

  private boolean isPermissionsDangerous(String[] paramArrayOfString)
  {
    int i = 0;
    while (true)
    {
      int j = paramArrayOfString.length;
      if (i >= j)
        return false;
      String str = paramArrayOfString[i];
      if ((!"android.permission.PROCESS_OUTGOING_CALLS".equals(str)) && (!"android.permission.READ_CONTACTS".equals(str)) && (!"android.permission.READ_SMS".equals(str)) && (!"android.permission.SEND_SMS".equals(str)) && (!"android.permission.CALL_PHONE".equals(str)) && (!"android.permission.CALL_PRIVILEGED".equals(str)) && ("android.permission.CHANGE_CONFIGURATION".equals(str)));
      i += 1;
    }
  }

  private void startBuy(Asset paramAsset)
  {
    Toast.makeText(this.mContext, 2131296452, 0).show();
  }

  private void startDownload(Asset paramAsset)
  {
    Intent localIntent = new Intent();
    int i = paramAsset._id;
    localIntent.putExtra("_id", i);
    String str1 = paramAsset.title;
    localIntent.putExtra("title", str1);
    int j = paramAsset.size;
    localIntent.putExtra("size", j);
    String str2 = paramAsset.pkgName;
    localIntent.putExtra("package", str2);
    String[] arrayOfString = paramAsset.permissions;
    localIntent.putExtra("permission", arrayOfString);
    Context localContext = this.mContext;
    String str3 = AssetPermissionsSubActivity.class.getName();
    localIntent.setClassName(localContext, str3);
    this.mContext.startActivity(localIntent);
  }

  private void startInstall(File paramFile, Asset paramAsset)
  {
    if (GlobalUtil.isSystemApp(this.mContext.getPackageManager()))
    {
      Intent localIntent1 = new Intent("android.intent.action.VIEW");
      int i = paramAsset._id;
      localIntent1.putExtra("_id", i);
      String str1 = paramAsset.title;
      localIntent1.putExtra("title", str1);
      String str2 = paramAsset.pkgName;
      localIntent1.putExtra("packageName", str2);
      Uri localUri1 = Uri.fromFile(paramFile);
      localIntent1.setDataAndType(localUri1, "application/vnd.android.package-archive");
      Context localContext = this.mContext;
      localIntent1.setClass(localContext, InstallAppProgress.class);
      this.mContext.startActivity(localIntent1);
    }
    while (true)
    {
      return;
      Intent localIntent2 = new Intent();
      localIntent2.setAction("android.intent.action.VIEW");
      localIntent2.addCategory("android.intent.category.DEFAULT");
      Uri localUri2 = Uri.fromFile(paramFile);
      localIntent2.setDataAndType(localUri2, "application/vnd.android.package-archive");
      this.mContext.startActivity(localIntent2);
    }
  }

  private void startUninstallProgress(Asset paramAsset)
  {
    Intent localIntent1;
    if (GlobalUtil.isSystemApp(this.mContext.getPackageManager()))
    {
      localIntent1 = new Intent("android.intent.action.VIEW");
      int i = paramAsset._id;
      localIntent1.putExtra("_id", i);
      String str1 = paramAsset.title;
      localIntent1.putExtra("title", str1);
    }
    while (true)
    {
      try
      {
        PackageManager localPackageManager = this.mContext.getPackageManager();
        String str2 = paramAsset.pkgName;
        ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(str2, 8192);
        localIntent1.putExtra("application_info", localApplicationInfo);
        Context localContext = this.mContext;
        localIntent1.setClass(localContext, UninstallAppProgress.class);
        this.mContext.startActivity(localIntent1);
        return;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        localNameNotFoundException.printStackTrace();
        continue;
      }
      String str3 = paramAsset.pkgName;
      Uri localUri = Uri.fromParts("package", str3, null);
      Intent localIntent2 = new Intent("android.intent.action.DELETE", localUri);
      try
      {
        this.mContext.startActivity(localIntent2);
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
      }
    }
  }

  public void addDownloadLogRequest()
  {
    if (this.mAsset == null);
    int i;
    do
    {
      return;
      ArrayList localArrayList = this.downloadArray;
      Integer localInteger1 = Integer.valueOf(this.mAsset._id);
      i = localArrayList.indexOf(localInteger1);
    }
    while (i == -1);
    this.downloadArray.remove(i);
    Request localRequest = new Request(0L, 65553);
    String str1;
    label76: String str2;
    if (this.mAsset.installed == 2)
    {
      str1 = "update";
      str2 = getFromPage();
      if (str2 != "Category")
        break label226;
      FilteredAppListActivity localFilteredAppListActivity1 = (FilteredAppListActivity)this.mContext;
      Object[] arrayOfObject1 = new Object[7];
      arrayOfObject1[0] = str1;
      arrayOfObject1[1] = str2;
      Integer localInteger2 = Integer.valueOf(localFilteredAppListActivity1.getCategory());
      arrayOfObject1[2] = localInteger2;
      Integer localInteger3 = Integer.valueOf(localFilteredAppListActivity1.getRankingType());
      arrayOfObject1[3] = localInteger3;
      Integer localInteger4 = Integer.valueOf(55537);
      arrayOfObject1[4] = localInteger4;
      Integer localInteger5 = Integer.valueOf(this.mAsset._id);
      arrayOfObject1[5] = localInteger5;
      String str3 = localFilteredAppListActivity1.getCategoryLabel();
      arrayOfObject1[6] = str3;
      localRequest.setData(arrayOfObject1);
    }
    while (true)
    {
      MarketService.getServiceInstance(this.mContext).getInstallUpdateLog(localRequest);
      break;
      str1 = "install";
      break label76;
      label226: if (str2 == "Ranking")
      {
        FilteredAppListActivity localFilteredAppListActivity2 = (FilteredAppListActivity)this.mContext;
        Object[] arrayOfObject2 = new Object[6];
        arrayOfObject2[0] = str1;
        arrayOfObject2[1] = str2;
        Integer localInteger6 = Integer.valueOf(55537);
        arrayOfObject2[2] = localInteger6;
        Integer localInteger7 = Integer.valueOf(55537);
        arrayOfObject2[3] = localInteger7;
        Integer localInteger8 = Integer.valueOf(localFilteredAppListActivity2.getPeriodType());
        arrayOfObject2[4] = localInteger8;
        Integer localInteger9 = Integer.valueOf(this.mAsset._id);
        arrayOfObject2[5] = localInteger9;
        localRequest.setData(arrayOfObject2);
        continue;
      }
      Object[] arrayOfObject3 = new Object[3];
      arrayOfObject3[0] = str1;
      arrayOfObject3[1] = str2;
      Integer localInteger10 = Integer.valueOf(this.mAsset._id);
      arrayOfObject3[2] = localInteger10;
      localRequest.setData(arrayOfObject3);
    }
  }

  public long getItemId(int paramInt)
  {
    return ((Asset)getItem(paramInt))._id;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Asset localAsset = (Asset)getItem(paramInt);
    DownloadService localDownloadService = DownloadService.getInstance(this.mContext);
    int i = localAsset._id;
    Handler localHandler = this.mDownloadStatusHandler;
    localDownloadService.setHandler(i, localHandler);
    this.thumb = null;
    ViewHolder localViewHolder;
    label286: label501: File localFile;
    if ((paramView == null) || (!(paramView.getTag() instanceof ViewHolder)))
    {
      paramView = this.mLayoutInflater.inflate(2130903058, paramViewGroup, 0);
      localViewHolder = new ViewHolder();
      localViewHolder.parent = paramView;
      LinearLayout localLinearLayout = (LinearLayout)paramView.findViewById(2131427415);
      localViewHolder.snippet = localLinearLayout;
      ImageView localImageView1 = (ImageView)paramView.findViewById(2131427373);
      localViewHolder.thumbnail = localImageView1;
      TextView localTextView1 = (TextView)paramView.findViewById(2131427417);
      localViewHolder.title = localTextView1;
      TextView localTextView2 = (TextView)paramView.findViewById(2131427375);
      localViewHolder.size = localTextView2;
      TextView localTextView3 = (TextView)paramView.findViewById(2131427420);
      localViewHolder.operation = localTextView3;
      RatingBar localRatingBar1 = (RatingBar)paramView.findViewById(2131427377);
      localViewHolder.rating = localRatingBar1;
      TextView localTextView4 = (TextView)paramView.findViewById(2131427418);
      localViewHolder.updateTime = localTextView4;
      TextView localTextView5 = (TextView)paramView.findViewById(2131427419);
      localViewHolder.notFrom = localTextView5;
      TextView localTextView6 = localViewHolder.operation;
      View.OnClickListener localOnClickListener = this.listener;
      localTextView6.setOnClickListener(localOnClickListener);
      paramView.setTag(localViewHolder);
      if (paramInt % 2 != 1)
        break label755;
      localViewHolder.snippet.setBackgroundResource(2130837594);
      TextView localTextView7 = localViewHolder.operation;
      Integer localInteger = Integer.valueOf(paramInt);
      localTextView7.setTag(localInteger);
      ImageView localImageView2 = localViewHolder.thumbnail;
      String str1 = String.valueOf(localAsset._id);
      String str2 = str1;
      localImageView2.setTag(str2);
      if (!(this.mContext instanceof FilteredAppListActivity))
        break label769;
      FilteredAppListActivity localFilteredAppListActivity = (FilteredAppListActivity)this.mContext;
      int j = localAsset._id;
      Drawable localDrawable1 = localFilteredAppListActivity.getThumbnail(paramInt, j);
      this.thumb = localDrawable1;
      label386: ImageView localImageView3 = localViewHolder.thumbnail;
      Drawable localDrawable2 = this.thumb;
      localImageView3.setImageDrawable(localDrawable2);
      this.thumb.setCallback(null);
      TextView localTextView8 = localViewHolder.title;
      String str3 = localAsset.title;
      localTextView8.setText(str3);
      TextView localTextView9 = localViewHolder.size;
      int k = localAsset.size;
      String str4 = getSize(k);
      localTextView9.setText(str4);
      if ((!(this.mContext instanceof UpdateAppListActivity)) || (localAsset.exist))
        break label904;
      localViewHolder.rating.setVisibility(8);
      localViewHolder.notFrom.setVisibility(0);
      if (this.mItemType == 901)
      {
        TextView localTextView10 = localViewHolder.updateTime;
        String str5 = localAsset.lastUpdate;
        String str6 = getUpdateTime(str5);
        localTextView10.setText(str6);
      }
      localViewHolder.priced = 0;
      localFile = getFile(localAsset);
      if (localFile == null)
        break label947;
      localViewHolder.operation.setText(2131296418);
      localViewHolder.operation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      localViewHolder.operation.setBackgroundResource(2130837596);
      label595: Context localContext = this.mContext;
      String str7 = localAsset.pkgName;
      int m = localAsset.versionCode;
      int n = localAsset._id;
      int i1 = PackageInstallInfo.getPackageInstalledStatus(localContext, str7, m, n);
      localAsset.installed = i1;
      if (localAsset.installed != 1)
        break label1149;
      if (!(this.mContext instanceof UpdateAppListActivity))
        break label1112;
      if (!localAsset.isSystem)
        break label1075;
      localViewHolder.operation.setText(2131296521);
      localViewHolder.operation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      localViewHolder.operation.setBackgroundResource(2130837586);
    }
    while (true)
    {
      int i2 = localAsset._id;
      if (checkAppDownloading(i2))
      {
        localViewHolder.operation.setBackgroundResource(2130837586);
        localViewHolder.operation.setText(2131296412);
      }
      return paramView;
      localViewHolder = (ViewHolder)paramView.getTag();
      break;
      label755: localViewHolder.snippet.setBackgroundResource(2130837595);
      break label286;
      label769: if ((this.mContext instanceof SearchAssetListActivity))
      {
        SearchAssetListActivity localSearchAssetListActivity = (SearchAssetListActivity)this.mContext;
        int i3 = localAsset._id;
        Drawable localDrawable3 = localSearchAssetListActivity.getThumbnail(paramInt, i3);
        this.thumb = localDrawable3;
        break label386;
      }
      if ((this.mContext instanceof CommonAppListActivity))
      {
        CommonAppListActivity localCommonAppListActivity = (CommonAppListActivity)this.mContext;
        int i4 = localAsset._id;
        Drawable localDrawable4 = localCommonAppListActivity.getThumbnail(paramInt, i4);
        this.thumb = localDrawable4;
        break label386;
      }
      if (!(this.mContext instanceof UpdateAppListActivity))
        break label386;
      UpdateAppListActivity localUpdateAppListActivity = (UpdateAppListActivity)this.mContext;
      int i5 = localAsset._id;
      Drawable localDrawable5 = localUpdateAppListActivity.getThumbnail(paramInt, i5);
      this.thumb = localDrawable5;
      break label386;
      label904: localViewHolder.rating.setVisibility(0);
      RatingBar localRatingBar2 = localViewHolder.rating;
      float f = localAsset.rating;
      localRatingBar2.setRating(f);
      localViewHolder.notFrom.setVisibility(8);
      break label501;
      label947: if (localAsset.price != 0.0F)
      {
        localViewHolder.priced = 1;
        if (localAsset.priceString == null)
        {
          String str8 = getPriceString(localAsset);
          localAsset.priceString = str8;
        }
        TextView localTextView11 = localViewHolder.operation;
        String str9 = localAsset.priceString;
        localTextView11.setText(str9);
        localViewHolder.operation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 2130837526, 0);
        localViewHolder.operation.setBackgroundResource(2130837577);
        break label595;
      }
      localViewHolder.operation.setText(2131296361);
      localViewHolder.operation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      localViewHolder.operation.setBackgroundResource(2130837577);
      break label595;
      label1075: localViewHolder.operation.setText(2131296360);
      localViewHolder.operation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      localViewHolder.operation.setBackgroundResource(2130837577);
      continue;
      label1112: localViewHolder.operation.setText(2131296358);
      localViewHolder.operation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      localViewHolder.operation.setBackgroundResource(2130837586);
      continue;
      label1149: if ((localAsset.installed != 2) || (localFile != null))
        continue;
      localViewHolder.operation.setText(2131296362);
      localViewHolder.operation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
      localViewHolder.operation.setBackgroundResource(2130837596);
    }
  }

  class ViewHolder
  {
    int _id;
    TextView notFrom;
    TextView operation;
    View parent;
    boolean priced;
    RatingBar rating;
    TextView size;
    LinearLayout snippet;
    ImageView thumbnail;
    TextView title;
    TextView updateTime;
  }
}

/* Location:           D:\android_tools\dex2jar-0.0.7.4-SNAPSHOT\classes.dex.dex2jar.jar
 * Qualified Name:     com.yingyonghui.market.FilteredAppListAdapter
 * JD-Core Version:    0.6.0
 */