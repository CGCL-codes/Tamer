package de.tud.android.locpairs;

import java.util.List;
import de.tud.android.locpairs.model.Player;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

/**
 * Has a list with all LobbyUsers and their positions.
 */
public class LobbyListAdapter extends BaseAdapter {

    /**
	 * The application Context in which this LobbyUserListAdapter is being used.
	 */
    private Context m_context;

    /**
	 * The dataset to which this LobbyUserListAdapter is bound.
	 */
    private List<Player> m_lobbyUserList;

    /**
	 * The position in the dataset of the currently selected LobbyUser.
	 */
    private int m_nSelectedPosition;

    /**
	 * Parameterized constructor that takes in the application Context in which
	 * it is being used and the Collection of users to which it is bound.
	 * m_nSelectedPosition will be initialized to Adapter.NO_SELECTION.
	 * 
	 * @param context
	 *            The application Context in which this LobbyUserListAdapter is being
	 *            used.
	 * 
	 * @param lobbyUserList
	 *            The Collection of String objects to which this LobbyUserListAdapter
	 *            is bound.
	 */
    public LobbyListAdapter(Context context, List<Player> lobbyUserList) {
        m_context = context;
        m_lobbyUserList = lobbyUserList;
        m_nSelectedPosition = Adapter.NO_SELECTION;
    }

    /**
	 * Accessor method for retrieving the position in the dataset of the
	 * currently selected User.
	 * 
	 * @return an integer representing the position in the dataset of the
	 *         currently selected User.
	 */
    public int getSelectedPosition() {
        return 0;
    }

    public int getCount() {
        return m_lobbyUserList.size();
    }

    public Object getItem(int arg0) {
        return m_lobbyUserList.get(arg0);
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if (arg1 == null) {
            LobbyUserView temp = new LobbyUserView(m_context, m_lobbyUserList.get(arg0));
            arg1 = temp;
            return arg1;
        } else {
            LobbyUserView temp = (LobbyUserView) arg1;
            temp.setLobbyUser(m_lobbyUserList.get(arg0));
            arg1 = temp;
            return arg1;
        }
    }

    public long getItemId(int position) {
        return position;
    }
}
