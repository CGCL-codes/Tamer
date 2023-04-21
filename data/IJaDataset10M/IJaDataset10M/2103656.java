package g2.routemaster.gui.wizards;

import g2.routemaster.gui.general.ECityCard;
import g2.routemaster.gui.general.ImageRegistry;
import g2.routemaster.model.CityCard;
import g2.routemaster.model.Hand;
import g2.routemaster.model.Player;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

public class ScoringDropCardsPage extends WizardPage {

    Group handCardsGroup;

    int numColumns = 7;

    protected ScoringDropCardsPage() {
        super("scoringDropCardsPage");
        setTitle("Scoring");
        setDescription("Player dropping citycards");
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        handCardsGroup = new Group(composite, SWT.NONE);
        handCardsGroup.setText("Cards on Hand");
        GridLayout layout = new GridLayout();
        layout.numColumns = numColumns;
        handCardsGroup.setLayout(layout);
        handCardsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
        updateHandCards();
        setControl(composite);
        setPageComplete(false);
    }

    void updateHandCards() {
        Player player = getPlayer();
        Hand hand = player.getHand();
        Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
        if (hand.sizeOfCityCard() > 0) {
            Iterator iter = hand.iteratorOfCityCard();
            while (iter.hasNext()) {
                CityCard card = (CityCard) iter.next();
                final CLabel cardPlace = new CLabel(handCardsGroup, SWT.PUSH);
                cardPlace.setData(card);
                cardPlace.setCursor(handCursor);
                cardPlace.setImage(getCardImage(card));
                cardPlace.setToolTipText(card.getCity().getName());
                cardPlace.addListener(SWT.MouseDown, cardSelectListener);
            }
        } else {
            final CLabel cardPlace = new CLabel(handCardsGroup, SWT.PUSH);
            cardPlace.setImage(getCardImage(null));
            cardPlace.setVisible(false);
        }
        handCardsGroup.layout();
    }

    Image getCardImage(CityCard card) {
        ECityCard city = ECityCard.UNKNOWN;
        if (card != null) {
            city = ECityCard.byNom(card.getCity().getName());
        }
        return ImageRegistry.getCity(city);
    }

    Player getPlayer() {
        ScoringWizard wizard = (ScoringWizard) getWizard();
        return wizard.player;
    }

    Listener cardSelectListener = new Listener() {

        public void handleEvent(Event e) {
            CLabel bSelectedCard = (CLabel) e.widget;
            if (bSelectedCard.getBackground().equals(getSelectedColor())) {
                bSelectedCard.setBackground(getUnselectedColor());
            } else {
                bSelectedCard.setBackground(getSelectedColor());
            }
            statusChanged();
        }
    };

    void unselectPlace(CLabel label) {
        label.setBackground(getUnselectedColor());
        label.setVisible(true);
    }

    void selectPlace(CLabel label) {
        label.setBackground(getSelectedColor());
    }

    Color getSelectedColor() {
        return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
    }

    Color getUnselectedColor() {
        return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
    }

    public void statusChanged() {
        setPageComplete(isPageComplete());
        getWizard().getContainer().updateButtons();
    }

    public boolean isPageComplete() {
        ScoringWizard wizard = (ScoringWizard) getWizard();
        Status status = validate();
        if (status.getSeverity() == IStatus.ERROR) {
            wizard.dropCompleted = false;
            setErrorMessage(status.getMessage());
            return false;
        } else {
            setErrorMessage(null);
        }
        saveDataToModel();
        return true;
    }

    Status validate() {
        Status status = validateRemainCards();
        if (status != null) {
            return status;
        } else {
            return new Status(IStatus.OK, "not_used", 0, "", null);
        }
    }

    Status validateRemainCards() {
        if (getPlayer().getHand().sizeOfCityCard() - getSelectedBoardLabel().length > 3) {
            return new Status(IStatus.ERROR, "not_used", 0, "Player can't have more than 3 cards after scoring.", null);
        }
        return null;
    }

    private void saveDataToModel() {
        ScoringWizard wizard = (ScoringWizard) getWizard();
        wizard.dropCompleted = true;
        wizard.dropCards = getSelectedCards();
    }

    CityCard[] getSelectedCards() {
        ArrayList<CityCard> result = new ArrayList<CityCard>();
        for (CLabel cardLabel : getSelectedBoardLabel()) {
            result.add((CityCard) cardLabel.getData());
        }
        return (CityCard[]) result.toArray(new CityCard[result.size()]);
    }

    CLabel[] getSelectedBoardLabel() {
        ArrayList<CLabel> result = new ArrayList<CLabel>();
        if (handCardsGroup != null) {
            for (Control child : handCardsGroup.getChildren()) {
                CLabel cardPlace = (CLabel) child;
                if (cardPlace.getBackground().equals(getSelectedColor())) {
                    result.add(cardPlace);
                }
            }
        }
        return (CLabel[]) result.toArray(new CLabel[result.size()]);
    }

    void onEnterPage() {
        ScoringWizard wizard = (ScoringWizard) getWizard();
        wizard.dropCompleted = false;
    }
}
