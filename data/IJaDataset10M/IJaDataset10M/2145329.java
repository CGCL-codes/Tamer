package de.objectinc.samsha.ui.information;

import de.objectinc.samsha.information.Information;

public interface IPresenceUIListener {

    public void onPrensenceChange(Information info);

    public void onSubscribe(Information info);
}
