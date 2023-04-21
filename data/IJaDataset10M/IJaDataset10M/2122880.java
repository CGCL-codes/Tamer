package cn.poco.food.firstpage;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.poco.bean.FoodPhoto;
import cn.poco.bean.Guest;
import cn.poco.bean.VoteLastImage;
import cn.poco.food.R;
import cn.poco.service.impl.PhotoImpl;
import cn.poco.tongji_poco.Tongji;
import cn.poco.util.AsyncLoadImageService;
import cn.poco.util.AsyncLoadImageServiceBig;
import cn.poco.util.Cons;
import cn.poco.util.Screen;
import cn.poco.util.AsyncLoadImageServiceBig.ImageCallback;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 美食美图图片墙
 * 
 * @author tanglx
 * 
 */
public class FoodPhotoListActivity extends Activity {

	public static final String TAG = "FoodPhotoListActivity";
	private Context context;
	// 视图
	private FrameLayout linearLayoutNew, linearLayoutHot;
	private GridView gridViewHot, gridViewNew;
	private FoodPhotoAdapt fAdapterHot, fAdapterNew;
	private LinearLayout preLayout, preLayout1;
	private RadioGroup foodTabRadioGroup;
	private RadioButton rButtonHot, rButtonNew;
	private Button foodPhotoResearch;
	// 错误处理
	private LinearLayout error;
	// 无数据处理
	private TextView noDataTextView, noDataTextView1;

	// 数据
	private Handler tHandler;
	private int type = 1;
	private ArrayList<FoodPhoto> listHot, listNew, listHotRe, ListNewRe;
//	ArrayList<Guest> listGuest, listGuestRe;
	private PhotoImpl pImpl;
	private ArrayList<VoteLastImage> listNewTwo,listNewTwoRe;

	// 条件
	private int lBase = 24, lRe = 12;
	private int sHot = 0, sNew = 0;

	// 标记
	private boolean IsLastDataHot = false;
	private boolean IsLastDataNew = false;
	private boolean isNewFirst = true;

	// 常量
	private static final int show_hot = 1;
	private static final int show_new = 2;

	protected void initView() {
		// 区分tab页
		foodPhotoResearch = (Button) findViewById(R.id.foodphoto_research);
		foodTabRadioGroup = (RadioGroup) findViewById(R.id.foodphoto_radiogroup);
		rButtonHot = (RadioButton) findViewById(R.id.radio_btn_hot);
		rButtonNew = (RadioButton) findViewById(R.id.radio_btn_new);
		linearLayoutHot = (FrameLayout) findViewById(R.id.foodphoto_hot_layout);
		gridViewHot = (GridView) findViewById(R.id.foodphoto_grid_hot);
		preLayout = (LinearLayout) findViewById(R.id.progressbar);
		preLayout.setBackgroundColor(Color.argb(30, 255, 255, 255));
		linearLayoutNew = (FrameLayout) findViewById(R.id.foodphoto_new_layout);
		gridViewNew = (GridView) findViewById(R.id.foodphoto_grid_new);
		preLayout1 = (LinearLayout) findViewById(R.id.progressbar1);
		preLayout1.setBackgroundColor(Color.argb(30, 255, 255, 255));
		// 处理错误信息
		noDataTextView = (TextView) findViewById(R.id.ErrorMessage);
		noDataTextView1 = (TextView) findViewById(R.id.ErrorMessage1);

		HandlerThread handlerThread = new HandlerThread(TAG);
		handlerThread.start();
		tHandler = new Handler(handlerThread.getLooper());

		Tongji.writeStrToFile(FoodPhotoListActivity.this, "event_id=103178&event_time="+String.valueOf(System.currentTimeMillis()).substring(0, 10));

	}

	void showTab(int status) {
		switch (status) {
		case show_hot:
			linearLayoutHot.setVisibility(View.VISIBLE);
			linearLayoutNew.setVisibility(View.GONE);
			Tongji.writeStrToFile(FoodPhotoListActivity.this, "event_id=103178&event_time="+String.valueOf(System.currentTimeMillis()).substring(0, 10));

			break;
		case show_new:
			linearLayoutHot.setVisibility(View.GONE);
			linearLayoutNew.setVisibility(View.VISIBLE);
			Tongji.writeStrToFile(FoodPhotoListActivity.this, "event_id=103177&event_time="+String.valueOf(System.currentTimeMillis()).substring(0, 10));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.foodphoto_list);
		initView();
		context = this;
		pImpl = new PhotoImpl();

		tHandler.post(new GetImage(type, sHot, lBase, 1));
		sHot = sHot + lBase;

		foodTabRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == rButtonHot.getId()) {
							type = 1;
							showTab(show_hot);
						}
						if (checkedId == rButtonNew.getId()) {
							type = 2;
							if (isNewFirst) {
								tHandler.post(new GetImage(type, sNew, lBase, 3));
								sNew = sNew + lBase;
							}
							isNewFirst = false;
							showTab(show_new);
						}
					}
				});

		foodPhotoResearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (type == 1) {
					if (IsLastDataHot == false) {
						tHandler.post(new GetImage(type, sHot, lRe, 2));
						sHot = sHot + lRe;
						foodPhotoResearch.setClickable(false);
						preLayout.setVisibility(View.VISIBLE);
					} else {
						Toast.makeText(context, "没有更多数据！", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					if (IsLastDataNew == false) {
						tHandler.post(new GetImage(type, sNew, lRe, 4));
						sNew = sNew + lRe;
						preLayout1.setVisibility(View.VISIBLE);
						foodPhotoResearch.setClickable(false);
					} else {
						Toast.makeText(context, "没有更多数据！", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});

		gridViewHot.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 跳转到餐厅详情
				Intent intent = new Intent(context, ResDetailActivity.class);
				intent.putExtra(Cons.RES_CODE,
						String.valueOf(listHot.get(position).getResId()));
				startActivity(intent);
			}
		});

		gridViewNew.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context,
						FoodPhotoDetailActivitiy.class);
				intent.putExtra(Cons.RESDATA, listNewTwo);
				intent.putExtra(Cons.RESDATAINDEX, position);
				startActivity(intent);
			}
		});

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:// 加载最赞
				fAdapterHot = new FoodPhotoAdapt(listHot, context, gridViewHot);
				gridViewHot.setAdapter(fAdapterHot);
				preLayout.setVisibility(View.GONE);
				gridViewHot.setVisibility(View.VISIBLE);
				break;
			case 2:// 刷新最赞
				preLayout.setVisibility(View.GONE);
				fAdapterHot.notifyDataSetChanged();
				break;
			case 3:// 加载最新
				fAdapterNew = new FoodPhotoAdapt(listNew, context, gridViewNew);
				gridViewNew.setAdapter(fAdapterNew);
				preLayout1.setVisibility(View.GONE);
				gridViewNew.setVisibility(View.VISIBLE);
				break;
			case 4:// 刷新最新
				preLayout1.setVisibility(View.GONE);
				fAdapterNew.notifyDataSetChanged();
				break;
			case 5:// 每次多执行这个
				foodPhotoResearch.setClickable(true);
				break;
			case 6:
				Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
				preLayout.setVisibility(View.GONE);
				break;
			case 7:
				Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
				preLayout1.setVisibility(View.GONE);
				break;
			case Cons.SHOW_ERROR:
				// 出错
				gridViewHot.setVisibility(View.GONE);
				gridViewNew.setVisibility(View.GONE);
				preLayout.setVisibility(View.GONE);
				preLayout1.setVisibility(View.GONE);
				noDataTextView.setVisibility(View.GONE);
				Toast.makeText(context, msg.obtain().toString(),
						Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		}
	};

	class GetImage implements Runnable {
		private int tLoadType;
		private int tType;
		private int ts;
		private int tl;

		public GetImage(int type, int s, int l, int loadType) {
			this.tLoadType = loadType;
			this.tType = type;
			this.ts = s;
			this.tl = l;
		}

		@Override
		public void run() {
			try {
				switch (tLoadType) {
				case 1:
					listHot = pImpl.getHotPhoto(tType, ts, tl);
					if (listHot != null) {
						if (listHot.size() > 0) {
							if (listHot.size() < 24) {
								IsLastDataHot = true;
							}
							System.out.println("listHot:" + listHot.size());
							handler.sendEmptyMessage(1);
						} else {
							handler.sendEmptyMessage(6);
						}
					} else {
						handler.sendEmptyMessage(6);
					}
					break;
				case 2:
					listHotRe = pImpl.getHotPhoto(tType, ts, tl);
					if (listHotRe != null) {
						listHot.addAll(0, listHotRe);
						if (listHotRe.size() < 12) {
							IsLastDataHot = true;
						}
						System.out.println("listHotRe:" + listHotRe.size());
						handler.sendEmptyMessage(2);
					}
					break;
				case 3:
					listNew = new ArrayList<FoodPhoto>();
					listNewTwo=pImpl.getNewPhotoTwo(ts, tl);
//					listGuest = pImpl.getNewPhoto(ts, tl);
					for (int i = 0; i < listNewTwo.size(); i++) {
						FoodPhoto p = new FoodPhoto();
						p.setImgUrl(listNewTwo.get(i).getImageUrl());
						listNew.add(p);
					}
					if (listNew != null) {
						if (listNew.size() > 0) {
							if (listNew.size() < 24) {
								IsLastDataNew = true;
							}
							System.out.println("listNew:" + listNew.size());
							handler.sendEmptyMessage(3);
						} else {
							handler.sendEmptyMessage(7);
						}
					} else {
						handler.sendEmptyMessage(7);
					}
					break;
				case 4:
					ListNewRe = new ArrayList<FoodPhoto>();
					listNewTwoRe=pImpl.getNewPhotoTwo(ts, tl);
//					listGuestRe = pImpl.getNewPhoto(ts, tl);
					for (int i = 0; i < listNewTwoRe.size(); i++) {
						FoodPhoto p = new FoodPhoto();
						p.setImgUrl(listNewTwoRe.get(i).getImageUrl());
						ListNewRe.add(p);
					}
					if (ListNewRe != null) {
						listNew.addAll(0, ListNewRe);
						listNewTwo.addAll(listNewTwoRe);
						if (ListNewRe.size() < 12) {
							IsLastDataNew = true;
						}
						handler.sendEmptyMessage(4);
					}
					break;
				default:
					break;
				}
				handler.sendEmptyMessage(5);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Cons.SHOW_ERROR;
				msg.obj = "网络错误";
				handler.sendMessage(msg);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.deep_menu_to_first, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.item01:
			intent = new Intent(this, cn.poco.food.MainTabActivity.class);
			cn.poco.food.MainTabActivity.tabHost.setCurrentTab(0);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}

/**
 * 美食美图适配器
 * 
 * @author tanglx
 * 
 */
class FoodPhotoAdapt extends BaseAdapter {
	GridView gridView;
	private ArrayList<FoodPhoto> vector;
	private Context context;
	private AsyncLoadImageServiceBig aLoader;
	int withAndHight = 119;
	Screen screen;
	ExecutorService threadPool;
	int imageWidth;

	FoodPhotoAdapt(ArrayList<FoodPhoto> vector, Context context,
			GridView gridView) {
		this.vector = vector;
		this.context = context;
		this.gridView = gridView;
		this.aLoader = new AsyncLoadImageServiceBig(context);
		screen=new Screen();
		threadPool=Executors.newCachedThreadPool();
		imageWidth= screen.getWidth(context)/4-10;
	}

	@Override
	public int getCount() {
		return vector.size();
	}

	@Override
	public Object getItem(int position) {

		return vector.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.food_photo_list_item, null);
			
			// convertView.setPadding(1, 1, 1, 1);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.srcFirstImage);
			viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth,imageWidth));
			viewHolder.imageView.setScaleType(ScaleType.CENTER_CROP);
			// viewHolder.imageView.setLayoutParams(new
			// LinearLayout.LayoutParams(withAndHight, withAndHight));
			// viewHolder.textView = (TextView)
			// convertView.findViewById(R.id.srcFirstTextView);
			// viewHolder.textView.setVisibility(View.GONE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final String imgUrl = vector.get(position).getImgUrl();
		if (imgUrl != null && !imgUrl.equals("")) {
			viewHolder.imageView.setTag(imgUrl); 
			Bitmap drawUserIcon = aLoader.loadBitmap(imgUrl,
					new ImageCallback() {
						@Override
						public void imageLoaded(Bitmap imageBitmap,
								String imageUrl) {
							ImageView imageViewByTag = (ImageView) gridView
									.findViewWithTag(imgUrl);
							if (imageViewByTag != null) {
								imageViewByTag.setImageBitmap(imageBitmap);
							}
						}
					}, imageWidth,imageWidth,"",threadPool);
			if (drawUserIcon == null) {
				viewHolder.imageView.setImageResource(R.drawable.load_image);
			} else {
				viewHolder.imageView.setImageBitmap(drawUserIcon);
			}
		} else {
			viewHolder.imageView.setImageResource(R.drawable.load_image);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView textView;
	}
}
