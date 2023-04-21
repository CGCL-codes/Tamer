package com.tamtamy.jatta.activity.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.tamtamy.jatta.R;
import com.tamtamy.jatta.TamTamyHelper;
import com.tamtamy.jatta.strongbox.JattaConstants;
import com.tamtamy.jttamobile.exception.JTTAException;

public class AddCommentActivity extends Activity {

    private Intent responseIntent = null;

    int result = RESULT_OK;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        final Button submit = (Button) findViewById(R.id.submit_comment);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    addComment();
                    setResult(RESULT_OK, responseIntent);
                } catch (JTTAException je) {
                    Intent result = new Intent();
                    result.putExtra(JattaConstants.ERROR_MESSAGE, je.getMessage());
                    setResult(com.tamtamy.jatta.strongbox.JattaConstants.RESPONSE_ACTIVITY_ERROR, result);
                }
                finish();
            }
        });
        final Button cancel = (Button) findViewById(R.id.cancel_comment);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToContent();
            }
        });
    }

    private void addComment() throws JTTAException {
        Long id = getIntent().getLongExtra("contentId", -1);
        final EditText comment = (EditText) findViewById(R.id.comment_text);
        String commentText = comment.getText().toString();
        if (null != commentText && commentText.trim().length() > 0) {
            TamTamyHelper.getInstance().postComment(String.valueOf(id), commentText);
            responseIntent = new Intent();
            responseIntent.putExtra("com.tamtamy.jatta.comment_text", commentText);
            responseIntent.putExtra("com.tamtamy.jatta.comment_contentId", id);
        }
    }

    private void goToContent() {
        setResult(result);
        this.finish();
    }
}
