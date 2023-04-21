package at.zweinull.druidsafe.client.android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.zweinull.druidsafe.client.android.R;
import at.zweinull.druidsafe.client.android.listeners.CancelActivityListener;
import at.zweinull.druidsafe.core.exceptions.AbstractErrorOccurredException;

/**
 * @version $Revision: $
 * @author Hannes Halenka <hannes@zweinull.at>
 * @since 0.1
 */
public abstract class AbstractPasswordItemActivity extends AbstractActivity implements View.OnClickListener {

    protected Button mButtonOk;

    protected Button mButtonCancel;

    protected EditText mInputTitle;

    protected EditText mInputUsername;

    protected EditText mInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_item);
        mButtonOk = (Button) findViewById(R.id.buttonOk);
        mButtonCancel = (Button) findViewById(R.id.buttonCancel);
        mInputTitle = (EditText) findViewById(R.id.inputTitle);
        mInputUsername = (EditText) findViewById(R.id.inputUsername);
        mInputPassword = (EditText) findViewById(R.id.inputPassword);
        mButtonOk.setOnClickListener(this);
        mButtonCancel.setOnClickListener(new CancelActivityListener(this));
    }

    protected void validateTitle(String title) throws AbstractPasswordItemActivity.ErrorOccurredException {
        if (title.length() < 1) {
            throw new AbstractPasswordItemActivity.ErrorOccurredException(AbstractPasswordItemActivity.ErrorOccurredException.ERROR_NO_TITLE);
        }
    }

    /**
     * @author Hannes Halenka <hannes@zweinull.at>
     * @since 0.1
     */
    public static class ErrorOccurredException extends AbstractErrorOccurredException {

        public static final int ERROR_NO_TITLE = AbstractErrorOccurredException.ERROR_FIRST + 1;

        public static final int ERROR_INVALID_ITEM_ID = ERROR_NO_TITLE + 1;

        public static final int ERROR_ITEM_DOES_NOT_EXIST = ERROR_INVALID_ITEM_ID + 1;

        public static final int ERROR_FIRST = ERROR_ITEM_DOES_NOT_EXIST + 1;

        private static final long serialVersionUID = 1L;

        public ErrorOccurredException(int errorCode) {
            super(errorCode);
        }
    }
}
