package de.tudresden.inf.rn.mobilis.android.xhunt.ui;

import java.util.ArrayList;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import de.tudresden.inf.rn.mobilis.android.xhunt.R;

/**
 * The Class DialogUsedTickets.
 */
public class DialogUsedTickets extends Dialog {

    /** The Constant DIALOG_ID. */
    public static final int DIALOG_ID = 302;

    /** The TableLayout which holds the used tickets. */
    private TableLayout mTableLayout;

    /** The applications context. */
    private Context mContext;

    /** The display of the players device. */
    private Display mDisplay;

    /** True if dialog already has content. */
    private boolean mHasContent = false;

    /**
	 * Instantiates a new DialogUsedTickets.
	 *
	 * @param context the context of the application
	 * @param display the display of the players device
	 */
    public DialogUsedTickets(Context context, Display display) {
        super(context);
        mContext = context;
        this.mDisplay = display;
        this.setContentView(R.layout.dialog_body);
        this.setTitle("Consumed Tickets");
        mTableLayout = (TableLayout) findViewById(R.id.dialog_list);
    }

    /**
	 * Clear content of this dialog.
	 */
    public void clearContent() {
        mTableLayout.removeAllViews();
        mHasContent = false;
    }

    /**
	 * Add a player and his used tickets to the TableLayout.
	 *
	 * @param playerIconId the id of the players icon
	 * @param playername the players name
	 * @param usedTicketIcons the used ticket icons
	 */
    public void addPlayer(int playerIconId, String playername, ArrayList<Bitmap> usedTicketIcons) {
        LinearLayout llIcon = new LinearLayout(mContext);
        llIcon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
        ImageView imgPlayerIcon = new ImageView(mContext);
        imgPlayerIcon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        imgPlayerIcon.setScaleType(ScaleType.CENTER);
        imgPlayerIcon.setBackgroundResource(playerIconId);
        llIcon.addView(imgPlayerIcon);
        LinearLayout llNameTickets = new LinearLayout(mContext);
        llNameTickets.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        llNameTickets.setOrientation(LinearLayout.VERTICAL);
        TextView tvPlayerName = new TextView(mContext);
        tvPlayerName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tvPlayerName.setText(playername);
        tvPlayerName.setPadding(10, 0, 0, 0);
        LinearLayout llTickets = new LinearLayout(mContext);
        llTickets.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        llTickets.setPadding(10, 2, 0, 0);
        for (Bitmap icon : usedTicketIcons) {
            ImageView imgIcon = new ImageView(mContext);
            imgIcon.setImageBitmap(icon);
            llTickets.addView(imgIcon);
        }
        llNameTickets.addView(tvPlayerName);
        llNameTickets.addView(llTickets);
        int displayWidth = mDisplay != null ? (int) (mDisplay.getWidth() * 0.9) : LayoutParams.FILL_PARENT;
        LinearLayout row = new LinearLayout(mContext);
        row.setLayoutParams(new LayoutParams(displayWidth, LayoutParams.WRAP_CONTENT));
        row.addView(llIcon);
        row.addView(llNameTickets);
        mTableLayout.addView(row);
        mHasContent = true;
    }

    /**
	 * Checks if dialog has content.
	 *
	 * @return true, if content is available
	 */
    public boolean hasContent() {
        return this.mHasContent;
    }
}
