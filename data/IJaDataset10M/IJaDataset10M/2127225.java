package de.tud.android.locpairs;

import de.tud.android.locpairs.model.Instance;
import de.tud.android.locpairs.model.Player;
import de.tud.android.mapbiq.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The Class GameInstanceView.
 */
public class GameInstanceView extends LinearLayout {

    /** The m_vw game instance text. */
    private TextView m_vwGameInstanceText;

    private TextView m_playerInstanceText;

    /** The m_game instance. */
    private Instance m_gameInstance;

    /**
	 * Basic Constructor that takes only takes in an application Context.
	 * 
	 * @param context
	 *            The application Context in which this view is being added.
	 * @param gameInstance
	 *            the game instance
	 */
    public GameInstanceView(Context context, Instance gameInstance) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.locpairs_game_view, this, true);
        m_vwGameInstanceText = (TextView) this.findViewById(R.id.OwnerTextView);
        m_playerInstanceText = (TextView) this.findViewById(R.id.PlayerTextView);
        setGameInstance(gameInstance);
        requestLayout();
    }

    /**
	 * Sets the game instance.
	 * 
	 * @param gameInstance
	 *            the new game instance
	 */
    public void setGameInstance(Instance gameInstance) {
        Log.v("gameinstances", "add new instance " + gameInstance.getInstanceJID());
        m_gameInstance = gameInstance;
        m_vwGameInstanceText.setText("Host: " + m_gameInstance.getOpener());
        m_vwGameInstanceText.setTextSize(14);
        m_playerInstanceText.setText(m_gameInstance.getPlayers().size() + " of " + m_gameInstance.getMaxMemberCount() + " Players");
        m_playerInstanceText.setTextSize(14);
    }
}
