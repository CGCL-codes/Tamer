package ru.nsu.ccfit.pm.econ.view.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.CardPane;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.wtk.ListViewItemStateListener;
import org.apache.pivot.wtk.ListViewSelectionListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Span;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtkx.Bindable;
import org.apache.pivot.wtkx.WTKX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.ccfit.pm.econ.common.engine.data.CompanyMessageType;
import ru.nsu.ccfit.pm.econ.common.engine.data.IUCompany;
import ru.nsu.ccfit.pm.econ.common.engine.data.IUCompanyMessage;
import ru.nsu.ccfit.pm.econ.common.engine.data.IUTextOnlyCompanyMessage;
import ru.nsu.ccfit.pm.econ.common.engine.roles.IUPlayer;
import ru.nsu.ccfit.pm.econ.common.view.ICompanyMessageListener;
import ru.nsu.ccfit.pm.econ.common.view.ITurnChangeListener;
import ru.nsu.ccfit.pm.econ.view.shared.CompanyRosterGateway;
import ru.nsu.ccfit.pm.econ.view.shared.GameTab;
import ru.nsu.ccfit.pm.econ.view.shared.GameTimeGateway;
import ru.nsu.ccfit.pm.econ.view.shared.PlayerRosterGateway;
import ru.nsu.ccfit.pm.econ.view.shared.guice.InjectionVisitable;
import ru.nsu.ccfit.pm.econ.view.shared.guice.InjectionVisitor;
import ru.nsu.ccfit.pm.econ.view.shared.localization.IFormatter;
import com.google.inject.Inject;

public class PublishScreen extends GameTab implements Bindable, InjectionVisitable, ICompanyMessageListener, ITurnChangeListener {

    static final Logger logger = LoggerFactory.getLogger(PublishScreen.class);

    @WTKX
    private ListView companyList;

    @WTKX
    private ListView newsList;

    @WTKX
    private CardPane messageTypeCP;

    @WTKX
    private Label titleLabel;

    @WTKX
    private TextArea descriptionTA;

    @WTKX
    private Label effectLabel;

    @WTKX
    private ListView recipientCheckList;

    @WTKX
    private PushButton selectAllPB;

    @WTKX
    private PushButton selectNonePB;

    @WTKX
    private Label numRecipientsLabel;

    @WTKX
    private PushButton publishPB;

    private IFormatter formatter;

    private PlayerRosterGateway playerRosterGw;

    private CompanyRosterGateway companyRosterGw;

    private GameTimeGateway gameTimeGw;

    private HashMap<Integer, Long> companyByIndex = new HashMap<Integer, Long>();

    private HashMap<Integer, IUCompanyMessage> messageByIndex = new HashMap<Integer, IUCompanyMessage>();

    private HashMap<Integer, Long> buddiesByIndex = new HashMap<Integer, Long>();

    @Inject
    public void setFormatter(IFormatter formatter) {
        this.formatter = formatter;
    }

    @Inject
    public void setPlayerRosterGateway(PlayerRosterGateway gateway) {
        if (this.playerRosterGw != null) {
            logger.warn("Redefining playerRosterGateway");
        }
        this.playerRosterGw = gateway;
    }

    @Inject
    public void setCompanyRosterGateway(CompanyRosterGateway gateway) {
        if (this.companyRosterGw != null) {
            logger.warn("Redefining companyRosterGateway");
            this.companyRosterGw.getCompanyMessageListeners().remove(this);
        }
        this.companyRosterGw = gateway;
        this.companyRosterGw.getCompanyMessageListeners().add(this);
    }

    @Inject
    public void setGameTimeGateway(GameTimeGateway gateway) {
        if (this.gameTimeGw != null) {
            logger.warn("Redefining gametimeRosterGateway");
            this.gameTimeGw.getTurnChangeListeners().remove(this);
        }
        this.gameTimeGw = gateway;
        this.gameTimeGw.getTurnChangeListeners().add(this);
    }

    @Override
    public void initialize() {
        logger.debug("instance is bound to wtkx");
        installListeners();
    }

    @Override
    public void acceptInjectionVisitor(InjectionVisitor visitor) {
        visitor.injectInto(this);
    }

    private void installListeners() {
        companyList.getListViewSelectionListeners().add(new ListViewSelectionListener.Adapter() {

            @Override
            public void selectedRangesChanged(ListView arg0, Sequence<Span> arg1) {
                updateCompaniesClicked();
            }
        });
        newsList.getListViewSelectionListeners().add(new ListViewSelectionListener.Adapter() {

            @Override
            public void selectedRangesChanged(ListView arg0, Sequence<Span> arg1) {
                updateNewsClicked();
            }
        });
        publishPB.getButtonPressListeners().add(new ButtonPressListener() {

            @Override
            public void buttonPressed(Button arg0) {
                publishButtonPressed();
            }
        });
        selectAllPB.getButtonPressListeners().add(new ButtonPressListener() {

            @Override
            public void buttonPressed(Button arg0) {
                selectAllButtonPressed();
            }
        });
        selectNonePB.getButtonPressListeners().add(new ButtonPressListener() {

            @Override
            public void buttonPressed(Button arg0) {
                selectNoneButtonPressed();
            }
        });
        recipientCheckList.getListViewItemStateListeners().add(new ListViewItemStateListener() {

            @Override
            public void itemCheckedChanged(ListView arg0, int arg1) {
                itemChecked();
            }
        });
    }

    protected void itemChecked() {
        int numRecipients = recipientCheckList.getCheckedIndexes().getLength();
        numRecipientsLabel.setText(Integer.toString(numRecipients));
    }

    protected void selectNoneButtonPressed() {
        for (int i = 0; i < recipientCheckList.getListData().getLength(); i++) {
            recipientCheckList.setItemChecked(i, false);
        }
    }

    protected void selectAllButtonPressed() {
        for (int i = 0; i < recipientCheckList.getListData().getLength(); i++) {
            recipientCheckList.setItemChecked(i, true);
        }
    }

    protected void publishButtonPressed() {
        int idx = newsList.getSelectedIndex();
        IUCompanyMessage msg = messageByIndex.get(idx);
        if (msg == null) return;
        if (msg.getType() == CompanyMessageType.OFFICIAL) {
            companyRosterGw.publishOfficialMessage(msg.getId());
        } else if (msg.getType() == CompanyMessageType.RUMOR) {
            List<Long> receivers = new LinkedList<Long>();
            for (int i = 0; i < recipientCheckList.getListData().getLength(); i++) {
                if (recipientCheckList.isItemChecked(i)) {
                    receivers.add(buddiesByIndex.get(i));
                }
            }
            if (receivers.isEmpty()) return;
            companyRosterGw.publishRumorMessage(msg.getId(), receivers);
        }
    }

    protected void updateNewsClicked() {
        recalcDescription();
        recalcLabels();
    }

    private void recalcLabels() {
        int idx = newsList.getSelectedIndex();
        IUCompanyMessage msg = messageByIndex.get(idx);
        if (msg == null) {
            titleLabel.setText("");
            effectLabel.setText("");
            return;
        }
        String text = msg.getMessage();
        double cCoefficient = msg.getCoefficientC();
        double kCoefficient = msg.getCoefficientK();
        if (msg.getType() == CompanyMessageType.OFFICIAL) {
            messageTypeCP.setSelectedIndex(0);
        } else {
            messageTypeCP.setSelectedIndex(1);
        }
        titleLabel.setText(formatter.formatNewsTitle(text));
        effectLabel.setText(formatter.formatNewsEffect(cCoefficient, kCoefficient));
    }

    protected void updateCompaniesClicked() {
        recalcNews();
        recalcDescription();
        recalcLabels();
    }

    private void initScreenControls() {
        recalcCompanies();
        recalcNews();
        recalcDescription();
        recalcLabels();
        recalcBuddies();
    }

    private void recalcBuddies() {
        org.apache.pivot.collections.List<String> data = new org.apache.pivot.collections.LinkedList<String>();
        buddiesByIndex.clear();
        int cnt = 0;
        for (IUPlayer player : playerRosterGw.getPlayerList()) {
            long playerId = player.getId();
            buddiesByIndex.put(cnt, playerId);
            data.add(player.getUnmodifiablePersonDescription().getName());
            cnt++;
        }
        recipientCheckList.setListData(data);
    }

    private void recalcCompanies() {
        companyByIndex.clear();
        org.apache.pivot.collections.List<String> data = new org.apache.pivot.collections.LinkedList<String>();
        int cnt = 0;
        for (IUCompany company : companyRosterGw.getCompanyList()) {
            long companyId = company.getId();
            companyByIndex.put(cnt, companyId);
            data.add(company.getName());
            cnt++;
        }
        companyList.setListData(data);
        logger.info("recalc companies");
    }

    private void recalcNews() {
        org.apache.pivot.collections.List<String> data = new org.apache.pivot.collections.LinkedList<String>();
        messageByIndex.clear();
        int idx = companyList.getSelectedIndex();
        Long companyId = companyByIndex.get(idx);
        IUCompany company = getCompanyById(companyId);
        if (company != null) {
            int cnt = 0;
            for (IUCompanyMessage msg : company.getAllMessages()) {
                if (msg.isPublished()) continue;
                messageByIndex.put(cnt, msg);
                data.add(msg.getTitle());
                cnt++;
            }
        }
        newsList.setListData(data);
        logger.info("recalc news");
    }

    private void recalcDescription() {
        int newsIdx = newsList.getSelectedIndex();
        IUCompanyMessage msg = messageByIndex.get(newsIdx);
        descriptionTA.setText("");
        if (msg == null) return;
        for (String line : msg.getMessage().split("\n")) {
            int insPoint = descriptionTA.getDocument().getCharacterCount() - 1;
            descriptionTA.setSelection(insPoint, 0);
            descriptionTA.insertText(line);
            descriptionTA.insertParagraph();
        }
        logger.info("recalc description");
    }

    private IUCompany getCompanyById(Long companyId) {
        if (companyId == null) return null;
        return companyRosterGw.getCompanyById(companyId);
    }

    @Override
    public void onCompanyMessageReceived(IUTextOnlyCompanyMessage companyMessage) {
        recalcNews();
        recalcDescription();
        recalcLabels();
    }

    @Override
    public void onCumulativeCompanyMessagesUpdate(Collection<? extends IUTextOnlyCompanyMessage> addedMessages) {
        recalcNews();
        recalcDescription();
        recalcLabels();
    }

    @Override
    public void onGameStart() {
        logger.info("onGameStart");
        initScreenControls();
    }

    @Override
    public void onTurnNumberChange(int newTurnNumber) {
        initScreenControls();
    }

    @Override
    public void onTurnStateChange(boolean isFinished) {
    }
}
