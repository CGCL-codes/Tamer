package cn.poco.food.firstpage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.poco.bean.Area;
import cn.poco.bean.Dish;
import cn.poco.bean.Restaurant;
import cn.poco.food.R;
import cn.poco.food.MainTabActivity;
import cn.poco.service.impl.SearchImpl;
import cn.poco.tongji_poco.Tongji;
import cn.poco.util.Cons;
import cn.poco.util.MyProgressDialog;
import cn.poco.util.SearchSuggestionSampleProvider;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.SearchRecentSuggestions;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

/**
 * 搜索结果页
 * 
 * @author tanglx
 * 
 */
public class SearchResultActivity extends Activity {
	private final static String TAG = "SearchResultActivity";
	private LinearLayout progress;
	private ProgressBar listFooterProgressbar;
	private TextView listFooterTextView;
	private Spinner spinner1, spinner2, spinner3;
	private ArrayList<Restaurant> list, list2;
	private ArrayList<Area> listArea;
	private ArrayList<Dish> listDish;
	private ListView listView;
	private Handler tHandler;
	private String cityCode, cityName, titleName;
	private ProgressDialog dialog;
	private SearchResultListAdapter searchListAdapter;
	private TextView titleTextView;
	private Button backBtn;
	private TextView acerTextView;
	// 搜索条件
	private String key;
	private String areaId;
	private String dishId;
	private int order;

	// 起始结果
	private int s = 0;
	private int l = 10;

	private SearchImpl searchImpl;
	private boolean isClick, isLastData = false;
	private OnItemSelectedListener spinnerItemSelectedListener1, spinnerItemSelectedListener2,
			spinnerItemSelectedListener3;
	private String tempTitleArea = null, tempTitleDish = null, tempCondition = null;
	private String query;
	private String chuangDish;
	// 更多
	LinearLayout viewMore;

	// 错误处理
	private LinearLayout error;
	private Button tautology;
	private Button setBtn;

	// 无数据处理
	private TextView noDataTextView;

	// 弹框数据
	private final static int UPDAE_DATA = 100;
	private final static int UPDAE_SUB_DATA = 101;
	private ArrayList<Area> data;
	private List<Map<String, String>> groupData;
	private List<List<Map<String, String>>> childData;
	private SimpleExpandableListAdapter adapter;
	private ExpandableListView expandableListView;
	private LinearLayout progressbar;

	public void initView() {
		isClick = false;

		titleTextView = (TextView) findViewById(R.id.show_title);
		backBtn = (Button) findViewById(R.id.top_left_btn);
		if (key != null && !key.equals("")) {
			titleTextView.setText(key);
			Tongji.writeStrToFile(SearchResultActivity.this,
					"event_id=103170&event_time=" + String.valueOf(System.currentTimeMillis()).substring(0, 10));
		}
		// 处理错误信息
		error = (LinearLayout) findViewById(R.id.error);
		error.setVisibility(View.GONE);
		tautology = (Button) findViewById(R.id.tautology);
		setBtn = (Button) findViewById(R.id.setnet);
		noDataTextView = (TextView) findViewById(R.id.ErrorMessage);

		progress = (LinearLayout) findViewById(R.id.progressbar);
		acerTextView = (TextView) findViewById(R.id.area_text);
		spinner1 = (Spinner) findViewById(R.id.area_spinner);
		spinner2 = (Spinner) findViewById(R.id.dish_spinner);
		spinner3 = (Spinner) findViewById(R.id.sort_spinner);
		listView = (ListView) findViewById(R.id.listview);

		// 处理传递过来的值
		titleName = getIntent().getExtras().getString(Cons.ACTIVITY_TOP_TITLE);
		chuangDish=getIntent().getExtras().getString(Cons.ACTIVITY_TOP_TITLE_DISH);
		if (titleName != null) {
			titleTextView.setText(titleName);
			setMyAreaTextview(titleName);
			tempTitleArea=titleName;
			titleName = "";
		}else {
			setMyAreaTextview("不限地区");
		}
		if (chuangDish!=null) {
			titleTextView.setText(chuangDish);
		}

		SharedPreferences sharedPreferences = getSharedPreferences(Cons.GPS_CITY_CODE, MODE_PRIVATE);
		cityCode = sharedPreferences.getString(Cons.CITY_CODE, "000000");
		areaId = getIntent().getExtras().getString(Cons.AREA_CODE);
		dishId = String.valueOf(getIntent().getExtras().getLong(Cons.SEARCH_DISHID));
		if (dishId.equals("0")) {
			dishId = null;
		}

		searchImpl = new SearchImpl(this);
		try {
			listArea = searchImpl.getArea(cityCode, this);
			String[] temp = new String[listArea.size() + 1];
			temp[0] = "全部区域";
			for (int j = 0; j < listArea.size(); j++) {
				temp[j + 1] = listArea.get(j).getName();
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, temp);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner1.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(Cons.CONN_ERROR);
		}

		try {
			
			listDish = searchImpl.getDishs(cityCode, "dish", this);
			String[] temp1 = new String[listDish.size() + 1];
			temp1[0] = "全部菜式";
			int currentIndex=-1;
			for (int j = 0; j < listDish.size(); j++) {
				temp1[j + 1] = listDish.get(j).getName();
				if (dishId!=null&&String.valueOf(listDish.get(j).getId()).equals(dishId)) {
					currentIndex=j;
				}
			}
			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner, temp1);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner2.setAdapter(adapter2);
			if (currentIndex!=-1) {
				//设置当前索引
				spinner2.setSelection(currentIndex+1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(Cons.CONN_ERROR);
		}

		try {
			// 食评数和加盟两个条件 接口没有
			String[] temp1 = new String[] { "不限条件", "按出品", "按服务", "按人均", "按加盟", "优惠" };
			ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spinner, temp1);
			adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner3.setAdapter(adapter3);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 更多的视图
		viewMore = (LinearLayout) LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.list_footer, null);
		listFooterProgressbar = (ProgressBar) viewMore.findViewById(R.id.list_footer_progress);
		listFooterTextView = (TextView) viewMore.findViewById(R.id.list_footer_text);

		backBtn.setVisibility(View.GONE);
	}

	// 接收全局搜索字符串数据
	private void doSearchQuery() {
		final Intent intent = getIntent();
		query = intent.getStringExtra(SearchManager.QUERY);
		// 保存搜索记录
		if (query != null) {
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
					SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
				key = query;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		doSearchQuery();
		initView();

		// 首次进入加载搜索结果数据
		HandlerThread handlerThread = new HandlerThread(TAG);
		handlerThread.start();
		tHandler = new Handler(handlerThread.getLooper());

		list = new ArrayList<Restaurant>();// 初始化listView;
		searchListAdapter = new SearchResultListAdapter(this, list, handler);
		listFooterProgressbar.setVisibility(View.GONE);
		listFooterTextView.setText("更多");
		listView.addFooterView(viewMore);
		listView.setAdapter(searchListAdapter);

		tHandler.post(runnable);

		setBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				startActivity(intent);
			}
		});

		tautology.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				spinner1.setClickable(true);
				spinner2.setClickable(true);
				spinner3.setClickable(true);
				progress.setVisibility(View.VISIBLE);
				error.setVisibility(View.GONE);
				tHandler.post(runnablere);
			}
		});

		spinnerItemSelectedListener1 = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("is spinner 1");
				if (isClick) {
					if (position == 0) {
						areaId = null;
						tempTitleArea = "全部地区";
					} else {
						areaId = listArea.get(position - 1).getKey();
						tempTitleArea = listArea.get(position - 1).getName();
					}
					setMyTitle();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		};

		spinnerItemSelectedListener2 = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("is spinner 2");
				tempTitleDish = null;
				if (isClick) {
					if (position == 0) {
						dishId = null;
						tempTitleDish = "全部菜式";
					} else {
						dishId = String.valueOf(listDish.get(position - 1).getId());
						tempTitleDish = listDish.get(position - 1).getName();
					}
					setMyTitle();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};

		spinnerItemSelectedListener3 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (isClick) {
					order = position;
					setMyTitle();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		};

		spinner1.setOnItemSelectedListener(spinnerItemSelectedListener1);
		spinner2.setOnItemSelectedListener(spinnerItemSelectedListener2);
		spinner3.setOnItemSelectedListener(spinnerItemSelectedListener3);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int count = listView.getCount();
				System.out.println("count:" + count + "   arg2:" + arg2);
				if ((arg2 == count - 1) && listView.getFooterViewsCount() > 0) {
					s = s + 10;
					tHandler.post(runnable);
					listFooterProgressbar.setVisibility(View.VISIBLE);
					listFooterTextView.setText("加载");
				} else {
					Restaurant restaurant = (Restaurant) arg0.getItemAtPosition(arg2);
					Intent intent = new Intent(SearchResultActivity.this, ResDetailActivity.class);
					intent.putExtra(Cons.RESDATA, restaurant);
					startActivity(intent);
				}

			}
		});
		
		acerTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// R.style.dialog
				CustomDialog cDialog=new CustomDialog(SearchResultActivity.this,android.R.style.Theme_Dialog);
				cDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
				cDialog.setTitle("请选择地区");
				cDialog.show();
			}
		});
	}

	public void setMyTitle() {
		Boolean flag = false;
		if (tempTitleArea == null) {
			tempTitleArea = "";
		}
		if (tempTitleDish == null) {
			flag = true;
		}
		if (tempCondition == null) {
			tempCondition = "";
		}
		if (flag) {
			titleTextView.setText((tempTitleArea + " " + tempCondition));
		} else {
			titleTextView.setText((" " + tempTitleArea + " " + tempTitleDish + " " + tempCondition));

		}
		dialog = MyProgressDialog.show(SearchResultActivity.this, "正在加载请稍后...");
		noDataTextView.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		tHandler.post(runnablere);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				isClick = true;
				spinner1.setClickable(true);
				spinner2.setClickable(true);
				spinner3.setClickable(true);

				if (dialog != null) {
					dialog.dismiss();
				}

				// 筛选条件没有数据
				if (list.size() > 0) {
					if (listView.getFooterViewsCount() > 0 && isLastData) {
						listView.removeFooterView(viewMore);
					} else {
						listFooterProgressbar.setVisibility(View.GONE);
						listFooterTextView.setText("更多");
					}
					progress.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
					searchListAdapter.notifyDataSetChanged();
				} else {
					handler.sendEmptyMessage(3);
				}
				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				if (isLastData) {
					listView.removeFooterView(viewMore);
				} else {
					if (listView.getFooterViewsCount() < 0) {
						listFooterProgressbar.setVisibility(View.GONE);
						listFooterTextView.setText("更多");
						listView.addFooterView(viewMore);
					}
				}
				searchListAdapter.notifyDataSetChanged();
				break;
			// 筛选得不到任何数据
			case 3:
				if (dialog != null) {
					dialog.dismiss();
				}
				listView.setVisibility(View.GONE);
				noDataTextView.setVisibility(View.VISIBLE);
				break;

			// 连接错误
			case Cons.CONN_ERROR:
				spinner1.setClickable(false);
				spinner2.setClickable(false);
				spinner3.setClickable(false);
				if (dialog != null) {
					dialog.dismiss();
				}
				progress.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				error.setVisibility(View.GONE);
				Toast.makeText(SearchResultActivity.this, "网络错误，请检查手机设置", Toast.LENGTH_SHORT).show();
				break;
			// 弹筐数据
			case UPDAE_DATA:
				groupData = new ArrayList<Map<String, String>>();
				childData = new ArrayList<List<Map<String, String>>>();
				for (int i = 0; i < data.size(); i++) {
					Map<String, String> groupMap = new HashMap<String, String>();
					groupMap.put("NAME", data.get(i).getName());
					groupData.add(groupMap);

					// 默认子数据
					List<Map<String, String>> children = new ArrayList<Map<String, String>>();

					Map<String, String> childmap = new HashMap<String, String>();
					childmap.put("NAME", "加载中");
					children.add(childmap);

					childData.add(children);
				}
				adapter = new SimpleExpandableListAdapter(SearchResultActivity.this, groupData,
						android.R.layout.simple_expandable_list_item_1, new String[] { "NAME" },
						new int[] { android.R.id.text1 }, childData, android.R.layout.simple_expandable_list_item_1,
						new String[] { "NAME" }, new int[] { android.R.id.text1 });

				expandableListView.setAdapter(adapter);
				progressbar.setVisibility(View.GONE);
				expandableListView.setVisibility(View.VISIBLE);
				break;
			case UPDAE_SUB_DATA:
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			try {
				list2 = searchImpl.getFilterRestaurant(cityCode, key, areaId, dishId, order, s, l);
				if (list2 != null) {
					int size = list2.size();
					if (size < 10) {
						isLastData = true;
					}
					list.addAll(list2);
					handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(3);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(Cons.CONN_ERROR);
			}
		}
	};

	Runnable runnablere = new Runnable() {
		@Override
		public void run() {
			try {
				list2 = searchImpl.getFilterRestaurant(cityCode, key, areaId, dishId, order, s, l);
				if (list2 != null) {
					int size = list2.size();
					if (size > 0) {
						if (size < 10) {
							isLastData = true;
						}
						list.clear();
						list.addAll(list2);
						s = 0;
						handler.sendEmptyMessage(2);
					} else {
						isLastData = true;
						handler.sendEmptyMessage(3);
					}
				} else {
					handler.sendEmptyMessage(3);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(Cons.CONN_ERROR);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.deep_menu_nologin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.item01:
			intent = new Intent(this, cn.poco.food.MainTabActivity.class);
			MainTabActivity.tabHost.setCurrentTab(0);
			startActivity(intent);
			break;
		case R.id.item02:
			intent = new Intent(this, cn.poco.food.MainTabActivity.class);
			MainTabActivity.tabHost.setCurrentTab(3);
			startActivity(intent);
			break;
		case R.id.item03:
			intent = new Intent(this, cn.poco.food.MainTabActivity.class);
			MainTabActivity.tabHost.setCurrentTab(2);
			startActivity(intent);
			break;
		case R.id.item04:
			intent = new Intent(this, cn.poco.food.MainTabActivity.class);
			MainTabActivity.tabHost.setCurrentTab(1);
			startActivity(intent);
			break;
		case R.id.item05:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class CustomDialog extends Dialog {

		public CustomDialog(Context context, int theme) {
			super(context, theme);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.customer_dialog_layout_3);
			expandableListView = (ExpandableListView) findViewById(R.id.expand_listview);
			expandableListView.setCacheColorHint(0);
			progressbar = (LinearLayout) findViewById(R.id.progressbar);
			tHandler.post(new GetArea());

			expandableListView.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
						long id) {
					String circle_code = childData.get(groupPosition).get(childPosition).get("ID");
					String circle_name = childData.get(groupPosition).get(childPosition).get("NAME");
					areaId = circle_code;
					tempTitleArea = circle_name;
					acerTextView.setText(circle_name.length()>5?circle_name.subSequence(0, 4)+"...":circle_name);
					CustomDialog.this.dismiss();
					setMyTitle();
					setMyAreaTextview(circle_name);
					return false;
				}
			});

			expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
				@Override
				public void onGroupExpand(int groupPosition) {
					tHandler.post(new GetSubArea(groupPosition, cityCode, data.get(groupPosition).getKey()));
				}
			});
		}

	}

	void setMyAreaTextview(String text){
		acerTextView.setText(text.length()>5?text.subSequence(0, 4)+"...":text);
	}
	
	private final class GetArea implements Runnable {

		@Override
		public void run() {
			SearchImpl search = new SearchImpl(SearchResultActivity.this);
			try {
				data = search.getArea(cityCode, SearchResultActivity.this);
				handler.sendEmptyMessage(UPDAE_DATA);
			} catch (Exception e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressbar.setVisibility(View.GONE);
					}
				});
				Log.e(TAG, e.toString());
				e.printStackTrace();
			}
		}
	}

	// 获取子数据
	private final class GetSubArea implements Runnable {
		private String citycode;
		private String lid;
		private int group_pos;

		public GetSubArea(int group_pos, String citycode, String lid) {
			this.group_pos = group_pos;
			this.citycode = citycode;
			this.lid = lid;
		}

		@Override
		public void run() {
			SearchImpl search = new SearchImpl(SearchResultActivity.this);
			try {
				ArrayList<Area> curChildData = search.getSubArea(citycode, lid);

				List<Map<String, String>> children = new ArrayList<Map<String, String>>();
				for (int j = 0; j < curChildData.size(); j++) {
					Map<String, String> childmap = new HashMap<String, String>();
					childmap.put("NAME", curChildData.get(j).getName());
					childmap.put("ID", curChildData.get(j).getKey());
					children.add(childmap);
				}
				childData.set(group_pos, children);
				handler.sendEmptyMessage(UPDAE_SUB_DATA);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
			}
		}
	}

}
