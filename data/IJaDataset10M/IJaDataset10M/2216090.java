package android.media.cts;

import com.android.cts.stub.R;
import android.app.Activity;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import java.util.List;

public class FaceDetectorStub extends Activity {

    public static final String IMAGE_ID = "imageId";

    private FaceView mFaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int imageId = getIntent().getIntExtra(IMAGE_ID, R.drawable.single_face);
        mFaceView = new FaceView(this, imageId);
        setContentView(mFaceView);
    }

    public List<Face> getDetectedFaces() {
        return mFaceView.detectedFaces;
    }
}
