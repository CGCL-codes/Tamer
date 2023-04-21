package cn.poco.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.util.Log;

import cn.poco.bean.Restaurant;
import cn.poco.service.IActiveRanklist;
import cn.poco.util.DownloadImage;
import cn.poco.util.UrlConnectionUtil;
import cn.poco.util.UrlMatchUtil;
import cn.poco.util.xmlparse.RestaurantXmlparse;

/**
 * 餐厅排行榜
 * @author tanglx
 *
 */
public class ActiveImpl implements IActiveRanklist {

	private final static String TAG = "impl";
	@Override
	public List<Restaurant> getSynthesis(String cityCode,Context context,int s,int l) throws Exception {
		String url = null;
		InputStream insteam = null;
		if (cityCode!=null) {
			url=IActiveRanklist.ACTIVTY_SYNTHESIS_PATH.replace("#a", cityCode).replace("#b", String.valueOf(s)).replace("#c", String.valueOf(l));
			url=UrlMatchUtil.matchUrl(url);
			insteam=UrlConnectionUtil.getRequest(url);
		}
		Log.i(TAG, "activei:"+url);
		return RestaurantXmlparse.getXml(insteam);
	}

	@Override
	public List<Restaurant> getRecommend(String cityCode,Context context,int s,int l) throws Exception {
		String url = null;
		InputStream insteam = null;
		
		if (cityCode!=null) {
			url=IActiveRanklist.ACTIVITY_RECOMMEND_PATH.replace("#a", cityCode).replace("#b", String.valueOf(s)).replace("#c", String.valueOf(l));
			url=UrlMatchUtil.matchUrl(url);
			insteam=UrlConnectionUtil.getRequest(url);
		}
		Log.i(TAG, "activei:"+url);
		return RestaurantXmlparse.getXml(insteam);
	}

	
	/**
	 * 只用十条
	 */
	@Override
	public List<Restaurant> getEidtRecommend(String cityCode, Context context,int s,int l) throws Exception {
		String url = null;
		InputStream insteam = null;
		
		if (cityCode!=null) {
			url=IActiveRanklist.ACTIVITY_EDITOR_PATH.replace("#a", cityCode).replace("#b", String.valueOf(s)).replace("#c", String.valueOf(l));
			url=UrlMatchUtil.matchUrl(url);
			insteam=UrlConnectionUtil.getRequest(url);
		}
		
		Log.i(TAG, "activei:"+url);
		return RestaurantXmlparse.getXml(insteam);
	}
	
	
}
