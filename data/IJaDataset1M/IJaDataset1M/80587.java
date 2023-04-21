package w_wind.demos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import w_wind.utils.BaseActivity;
import w_wind.utils.POJOAdapterFiller;
import w_wind.utils.POJOTextFiller;
import w_wind.utils.WToast;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ZoomButtonsController;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ZoomButtonsController.OnZoomListener;

public class MainActivity extends BaseActivity {

    public static final int demoID = 15;

    private WToast toast;

    public static final String TEXT_COLUMN = "text_item";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = new WToast(this);
        selectDemo();
        showMetrics();
    }

    private void showMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    private void selectDemo() {
        switch(demoID) {
            case 1:
                textViewDemo();
                break;
            case 2:
                edit_text_demo();
                break;
            case 3:
                image_view_demo();
                break;
            case 4:
                button_demo();
                break;
            case 5:
                zoom_demo();
                break;
            case 6:
                image_demo();
                break;
            case 7:
                style_demo();
                break;
            case 8:
                include_layout_demo();
                break;
            case 9:
                multiple_listener_demo();
                break;
            case 10:
                activity_dateShare_demo();
                break;
            case 11:
                pojo_fill_demo();
                break;
            case 12:
                grid_view_demo();
                break;
            case 13:
                multi_thread_demo();
                break;
            case 14:
                progressbar_demo();
                break;
            case 15:
                view_stub_demo();
                break;
        }
        int lastField;
    }

    private void textViewDemo() {
        setContentView(R.layout.text_view_demo);
    }

    private void edit_text_demo() {
        setContentView(R.layout.edit_text_demo);
    }

    private void image_view_demo() {
        setContentView(R.layout.image_view_demo);
    }

    private void button_demo() {
        setContentView(R.layout.button_demo);
        TextView inforText = (TextView) findViewById(R.id.txtInfor);
        ButtonDemoHandler bdh = new ButtonDemoHandler(inforText);
        Button btn = null;
        btn = ((Button) findViewById(R.id.btn));
        btn.setOnClickListener(bdh);
        btn.setOnLongClickListener(bdh);
        btn.setOnTouchListener(bdh);
        ImageButton[] ibtn = null;
        ibtn = new ImageButton[] { ((ImageButton) findViewById(R.id.ibtn)), ((ImageButton) findViewById(R.id.ibtnComplex)) };
        for (int i = 0; i < ibtn.length; i++) {
            setOnClickListener(ibtn[i], bdh);
            setOnLongClickListener(ibtn[i], bdh);
            setOnTouchListener(ibtn[i], bdh);
        }
    }

    private void image_demo() {
        setContentView(R.layout.image_demo);
        ImageView image = (ImageView) findViewById(R.id.image);
        ImageDemoHandler handler = new ImageDemoHandler(image);
        ((Spinner) findViewById(R.id.spinner_matrix)).setOnItemSelectedListener(handler);
    }

    private void zoom_demo() {
        setContentView(R.layout.zoom_demo);
        ImageView zoomImage = (ImageView) findViewById(R.id.zoomImage);
        ZoomDemoHandler l = new ZoomDemoHandler(zoomImage);
        setOnTouchListener(zoomImage, l);
    }

    private void style_demo() {
        setContentView(R.layout.style_demo);
    }

    private void include_layout_demo() {
        setContentView(R.layout.include_layout_demo);
    }

    private void multiple_listener_demo() {
        setContentView(R.layout.multiple_listener_demo);
        Button btn = (Button) findViewById(R.id.btnClick);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                toast.showMessage("Hello!");
            }
        });
        btn.setOnClickListener(new OnClickListener() {

            private boolean red_or_white;

            @Override
            public void onClick(View v) {
                if (red_or_white) {
                    v.setBackgroundColor(Color.RED);
                } else {
                    v.setBackgroundColor(Color.WHITE);
                }
                red_or_white = !red_or_white;
            }
        });
    }

    private void activity_dateShare_demo() {
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
        finish();
    }

    private void pojo_fill_demo() {
        setContentView(R.layout.pojo_fill_demo);
        MyPOJO pojo = new MyPOJO();
        pojo.setName("����").setAge(21).setHigh(1.77f).setSex('Ů');
        final POJOTextFiller filler = new POJOTextFiller(this);
        filler.fillPOJO(pojo, "txt", R.id.class);
        Map<String, ArrayList<Object>> relations = new HashMap<String, ArrayList<Object>>();
        ArrayList<Object> sexList = new ArrayList<Object>();
        sexList.add("��");
        sexList.add("Ů");
        relations.put("sex", sexList);
        final POJOAdapterFiller filler2 = new POJOAdapterFiller(this, relations);
        filler2.fillPOJO(pojo, "sp", R.id.class);
        ((Button) findViewById(R.id.btnSubmit)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MyPOJO pojo = null;
                try {
                    pojo = filler.getPOJO(MyPOJO.class, "txt", R.id.class, null);
                    pojo = filler2.getPOJO(MyPOJO.class, "sp", R.id.class, pojo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (pojo != null) {
                    showPOJO(pojo);
                } else {
                    toast.showMessage("ת��ʧ�ܣ�");
                }
            }

            private void showPOJO(MyPOJO pojo) {
                toast.showMessage(pojo.toString());
            }
        });
    }

    private void grid_view_demo() {
        setContentView(R.layout.gridview_demo);
        GridView gridView = (GridView) findViewById(R.id.myGridView);
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.icon);
            map.put("text", "text" + i);
            items.add(map);
        }
        ListAdapter adapter = new SimpleAdapter(this, items, R.layout.grid_item, new String[] { "image", "text" }, new int[] { R.id.image, R.id.text });
        gridView.setAdapter(adapter);
        GridViewDemoHandler handler = new GridViewDemoHandler();
        gridView.setOnItemClickListener(handler);
        gridView.setOnItemSelectedListener(handler);
    }

    private void multi_thread_demo() {
        Intent intent = new Intent(this, MultiThreadActivity3.class);
        startActivity(intent);
        finish();
    }

    private void progressbar_demo() {
        startActivity(new Intent(this, ProgressBarActivity.class));
        finish();
    }

    private void view_stub_demo() {
        setContentView(R.layout.view_stub_demo);
        findViewById(R.id.btnLoad).setOnClickListener(new ViewStubDemoHandler());
    }

    private void lastMethod() {
    }

    private void setOnClickListener(View v, OnClickListener l) {
        v.setOnClickListener(l);
    }

    private void setOnLongClickListener(View v, OnLongClickListener l) {
        v.setOnLongClickListener(l);
    }

    private void setOnTouchListener(View v, OnTouchListener l) {
        v.setOnTouchListener(l);
    }

    /**
     * ButtonDemo���¼������࣬�����˵���������������¼� 
     */
    class ButtonDemoHandler implements OnClickListener, OnLongClickListener, OnTouchListener {

        private TextView infor;

        public ButtonDemoHandler(TextView infor) {
            this.infor = infor;
        }

        @Override
        public void onClick(View v) {
            infor.setText(v.getTag() + "�����");
        }

        @Override
        public boolean onLongClick(View v) {
            infor.setText(v.getTag() + "�������");
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            infor.setText(v.getTag() + "��������");
            return false;
        }
    }

    class ZoomDemoHandler implements OnTouchListener {

        private ImageView handlerImage;

        private ZoomButtonsController controller;

        public ZoomDemoHandler(ImageView imageView) {
            handlerImage = imageView;
            controller = new ZoomButtonsController(imageView);
            controller.setOnZoomListener(new ZoomHandler());
            controller.setZoomOutEnabled(false);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            controller.setVisible(true);
            return false;
        }

        class ZoomHandler2 implements OnZoomListener {

            @Override
            public void onVisibilityChanged(boolean visible) {
            }

            @Override
            public void onZoom(boolean zoomIn) {
                toast.showMessage("zoom!");
            }
        }

        class ZoomHandler implements OnZoomListener {

            private int level = 0;

            @Override
            public void onVisibilityChanged(boolean visible) {
                if (!visible) handlerImage.requestFocus();
            }

            @Override
            public void onZoom(boolean zoomIn) {
                Matrix matrix = new Matrix(handlerImage.getImageMatrix());
                if (zoomIn) {
                    level += 1;
                    matrix.setScale((float) (1 + 0.1 * level), (float) (1 + 0.1 * level));
                } else {
                    level -= 1;
                    matrix.setScale((float) (1 - 0.1 * level), (float) (1 - 0.1 * level));
                }
                if (level == 0) {
                    controller.setZoomOutEnabled(false);
                    controller.setZoomInEnabled(true);
                } else if (level == 5) {
                    controller.setZoomOutEnabled(true);
                    controller.setZoomInEnabled(false);
                }
                handlerImage.setImageMatrix(matrix);
            }
        }
    }

    class ImageDemoHandler implements OnItemSelectedListener {

        private ImageView handlerImage;

        public ImageDemoHandler(ImageView image) {
            handlerImage = image;
        }

        private void doZoom(boolean zoomIn) {
            Matrix matrix = getMatrix();
            float scale = 0f;
            if (zoomIn) {
                scale = 1.5f;
            } else {
                scale = 0.5f;
            }
            matrix.postScale(scale, scale);
            applyMatrix(matrix);
        }

        private void doRotate() {
            Matrix matrix = getMatrix();
            float dt = 180f;
            matrix.postRotate(dt);
            applyMatrix(matrix);
        }

        private void doTranslate() {
        }

        private Matrix getMatrix() {
            return new Matrix(handlerImage.getImageMatrix());
        }

        private void applyMatrix(Matrix matrix) {
            handlerImage.setImageMatrix(matrix);
        }

        @Override
        public void onItemSelected(AdapterView<?> parentAdapterView, View selectedView, int position, long id) {
            switch(position) {
                case 0:
                    doZoom(true);
                    break;
                case 1:
                    doZoom(false);
                    break;
                case 2:
                    doRotate();
                    break;
                case 3:
                    break;
                case 4:
                    doTranslate();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class GridViewDemoHandler implements OnItemSelectedListener, OnItemClickListener {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            toast.showMessage("ѡ����" + arg2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            toast.showMessage("�����" + arg2);
        }
    }

    class ViewStubDemoHandler implements OnClickListener {

        @Override
        public void onClick(View v) {
            ViewStub stub = findST(R.id.stub);
            if (stub != null) {
                stub.inflate();
                findB(R.id.btnLoad).setEnabled(false);
            }
        }
    }
}
