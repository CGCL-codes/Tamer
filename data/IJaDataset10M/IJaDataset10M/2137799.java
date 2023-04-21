package com.onesadjam.yagrac;

import java.util.List;
import com.onesadjam.yagrac.xml.Book;
import com.onesadjam.yagrac.xml.ResponseParser;
import com.onesadjam.yagrac.xml.Review;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateStatusActivity extends Activity {

    private String _UserId;

    private Spinner _BookSpinner;

    private EditText _Comment;

    private EditText _PageNumber;

    private Button _UpdateButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatestatus);
        _BookSpinner = (Spinner) findViewById(R.id._UpdateBookSpinner);
        _UpdateButton = (Button) findViewById(R.id._UpdateStatusButton);
        _Comment = (EditText) findViewById(R.id._UpdateStatusCommentEditText);
        _PageNumber = (EditText) findViewById(R.id._UpdateStatusPageNumberEditText);
        _UserId = getIntent().getExtras().getString("com.onesadjam.yagrac.UserId");
        BookAdapter bookSpinnerAdapter = new BookAdapter(this);
        try {
            List<Review> reviews = ResponseParser.GetBooksOnShelf("currently-reading", _UserId).get_Reviews();
            if (reviews.size() == 0) {
                _BookSpinner.setVisibility(View.GONE);
                findViewById(R.id._UpdateStatusPageNumberEditText).setVisibility(View.GONE);
                findViewById(R.id._UpdateStatusPageNumberLabel).setVisibility(View.GONE);
            }
            for (int i = 0; i < reviews.size(); i++) {
                bookSpinnerAdapter.add(reviews.get(i).get_Book());
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        _BookSpinner.setAdapter(bookSpinnerAdapter);
        _UpdateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (_BookSpinner.getVisibility() == View.GONE) {
                        ResponseParser.PostStatusUpdate(_Comment.getText().toString());
                    } else {
                        ResponseParser.PostStatusUpdate(((Book) _BookSpinner.getSelectedItem()).get_Id(), _PageNumber.getText().toString(), _Comment.getText().toString());
                    }
                    Toast.makeText(v.getContext(), "status updated", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
