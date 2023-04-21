package ru.caffeineim.protocols.icq.integration.listeners;

import java.util.EventListener;
import ru.caffeineim.protocols.icq.integration.events.MetaAffilationsUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaBasicUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaEmailUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaInterestsUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaMoreUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaNoteUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaShortUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.MetaWorkUserInfoEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationFailedEvent;
import ru.caffeineim.protocols.icq.integration.events.UINRegistrationSuccessEvent;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 *   @author Samolisov Pavel
 *   @author Egor Baranov 
 */
public interface MetaInfoListener extends EventListener {

    public void onShortUserInfo(MetaShortUserInfoEvent e);

    public void onBasicUserInfo(MetaBasicUserInfoEvent e);

    public void onEmailUserInfo(MetaEmailUserInfoEvent e);

    public void onWorkUserInfo(MetaWorkUserInfoEvent e);

    public void onInterestsUserInfo(MetaInterestsUserInfoEvent e);

    public void onMoreUserInfo(MetaMoreUserInfoEvent e);

    public void onNotesUserInfo(MetaNoteUserInfoEvent e);

    public void onAffilationsUserInfo(MetaAffilationsUserInfoEvent e);

    public void onRegisterNewUINSuccess(UINRegistrationSuccessEvent e);

    public void onRegisterNewUINFailed(UINRegistrationFailedEvent e);
}
