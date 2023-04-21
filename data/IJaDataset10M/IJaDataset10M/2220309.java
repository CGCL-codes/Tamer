package dk.mirasola.systemtraining.bridgewidgets.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import dk.mirasola.systemtraining.bridgewidgets.client.i18n.BridgeTexts;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Bid;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.BiddingSequence;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Seat;

public class BiddingSequenceTable extends Composite {

    interface BiddingSequenceTableUiBinder extends UiBinder<Widget, BiddingSequenceTable> {
    }

    private static BiddingSequenceTableUiBinder uiBinder = GWT.create(BiddingSequenceTableUiBinder.class);

    private static BridgeTexts texts = GWT.create(BridgeTexts.class);

    @UiField
    Grid grid;

    @UiField
    HTML northLabel;

    @UiField
    HTML eastLabel;

    @UiField
    HTML southLabel;

    @UiField
    HTML westLabel;

    public BiddingSequenceTable() {
        initWidget(uiBinder.createAndBindUi(this));
        northLabel.setHTML(texts.northShort());
        eastLabel.setHTML(texts.eastShort());
        southLabel.setHTML(texts.southShort());
        westLabel.setHTML(texts.westShort());
    }

    public void setBiddingSequence(BiddingSequence biddingSequence) {
        grid.resizeRows(biddingSequence.getRoundsCount() + 1);
        if (biddingSequence != null) {
            for (int round = 1; round <= biddingSequence.getRoundsCount(); round++) {
                int col = 0;
                for (Seat seat : Seat.values()) {
                    Bid bid = biddingSequence.getBid(round, seat);
                    if (bid == null) {
                        grid.setText(round, col, "");
                    } else {
                        grid.setHTML(round, col, RenderUtil.renderBid(bid));
                    }
                    col++;
                }
            }
        }
    }
}
