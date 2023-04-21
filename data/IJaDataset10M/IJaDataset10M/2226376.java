package g2.routemaster.gui.wizards;

import g2.routemaster.gui.general.ECityCard;
import g2.routemaster.gui.general.ImageRegistry;
import g2.routemaster.model.CityCard;
import g2.routemaster.model.PlayedStack;
import g2.routemaster.model.Player;
import g2.routemaster.model.Region;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

public class ScoringPlaceHousesPage extends WizardPage {

    Group boardCardsGroup;

    Button bSameRegion;

    Button bDifferentRegion;

    int numColumns = 7;

    protected ScoringPlaceHousesPage() {
        super("scoringPage");
        setTitle("Scoring");
        setDescription("Player placing houses");
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        Group gStrategy = new Group(composite, SWT.NONE);
        gStrategy.setText("Placing strategy:");
        gStrategy.setLayout(new GridLayout());
        bDifferentRegion = new Button(gStrategy, SWT.RADIO);
        bDifferentRegion.setText("houses into different regions");
        bDifferentRegion.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                clearHouses();
                initDifferentRegionSelection();
                statusChanged();
            }
        });
        bSameRegion = new Button(gStrategy, SWT.RADIO);
        bSameRegion.setText("houses into one region");
        bSameRegion.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                clearHouses();
                initSameRegionSelection();
                statusChanged();
            }
        });
        boardCardsGroup = new Group(composite, SWT.NONE);
        boardCardsGroup.setText("Cards on Board");
        GridLayout layout = new GridLayout();
        layout.numColumns = numColumns;
        boardCardsGroup.setLayout(layout);
        boardCardsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
        updateBoardCards();
        setControl(composite);
        setPageComplete(false);
    }

    void updateBoardCards() {
        Player player = getPlayer();
        PlayedStack playedStack = player.getPlayedStack();
        Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
        if (playedStack.sizeOfCityCard() > 0) {
            Iterator iter = playedStack.iteratorOfCityCard();
            while (iter.hasNext()) {
                CityCard card = (CityCard) iter.next();
                final CLabel cardPlace = new CLabel(boardCardsGroup, SWT.PUSH);
                cardPlace.setData(card);
                cardPlace.setCursor(handCursor);
                cardPlace.setToolTipText(card.getCity().getName());
                if (player.hasABranchIn(card.getCity())) {
                    cardPlace.setImage(new Image(Display.getCurrent(), getCardImage(card), SWT.IMAGE_GRAY));
                    cardPlace.setEnabled(false);
                } else {
                    cardPlace.setImage(getCardImage(card));
                }
                cardPlace.addListener(SWT.MouseDown, cardSelectListener);
            }
        } else {
            final CLabel cardPlace = new CLabel(boardCardsGroup, SWT.PUSH);
            cardPlace.setImage(getCardImage(null));
            cardPlace.setVisible(false);
        }
        boardCardsGroup.layout();
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

    void initSameRegionSelection() {
        Region region = null;
        for (Control control : boardCardsGroup.getChildren()) {
            CLabel lCard = (CLabel) control;
            if (lCard.isEnabled() && lCard.isVisible()) {
                CityCard card = (CityCard) lCard.getData();
                Region cityRegion = card.getCity().getRegion();
                if (region == null || cityRegion.equals(region)) {
                    selectPlace(lCard);
                    region = cityRegion;
                }
            }
        }
    }

    void initDifferentRegionSelection() {
        ArrayList<Region> selected = new ArrayList<Region>();
        for (Control control : boardCardsGroup.getChildren()) {
            CLabel lCard = (CLabel) control;
            if (lCard.isEnabled() && lCard.isVisible()) {
                CityCard card = (CityCard) lCard.getData();
                Region cityRegion = card.getCity().getRegion();
                if (!selected.contains(cityRegion)) {
                    selectPlace(lCard);
                    selected.add(cityRegion);
                }
            }
        }
    }

    void clearHouses() {
        for (Control control : boardCardsGroup.getChildren()) {
            CLabel lCard = (CLabel) control;
            if (lCard.isEnabled()) {
                unselectPlace(lCard);
            }
        }
    }

    Listener cardSelectListener = new Listener() {

        public void handleEvent(Event e) {
            CLabel bSelectedCard = (CLabel) e.widget;
            if (bSelectedCard.getBackground().equals(getSelectedColor())) {
                return;
            }
            CityCard card = (CityCard) bSelectedCard.getData();
            Region region = card.getCity().getRegion();
            if (bSameRegion.getSelection()) {
                for (Control control : boardCardsGroup.getChildren()) {
                    CLabel lCard = (CLabel) control;
                    if (lCard.isEnabled()) {
                        CityCard nextCard = (CityCard) lCard.getData();
                        if (nextCard.getCity().getRegion().equals(region)) {
                            selectPlace(lCard);
                        } else {
                            unselectPlace(lCard);
                        }
                    }
                }
            } else {
                for (Control control : boardCardsGroup.getChildren()) {
                    CLabel lCard = (CLabel) control;
                    if (lCard.isEnabled()) {
                        CityCard nextCard = (CityCard) lCard.getData();
                        if (nextCard.getCity().getRegion().equals(region)) {
                            if (lCard.equals(bSelectedCard)) {
                                selectPlace(lCard);
                            } else {
                                unselectPlace(lCard);
                            }
                        }
                    }
                }
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
        saveDataToModel();
        return true;
    }

    private void saveDataToModel() {
        ScoringWizard wizard = (ScoringWizard) getWizard();
        wizard.placeCompleted = true;
        wizard.cityCards = getSelectedCards();
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
        for (Control child : boardCardsGroup.getChildren()) {
            CLabel cardPlace = (CLabel) child;
            if (cardPlace.getBackground().equals(getSelectedColor())) {
                result.add(cardPlace);
            }
        }
        return (CLabel[]) result.toArray(new CLabel[result.size()]);
    }

    void onEnterPage() {
        ScoringWizard wizard = (ScoringWizard) getWizard();
        wizard.placeCompleted = false;
    }

    public boolean canFlipToNextPage() {
        return true;
    }
}
