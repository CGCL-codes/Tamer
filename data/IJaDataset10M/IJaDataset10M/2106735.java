package mrusanov.fantasyruler.game.ui.components.diplomacy.action;

import java.awt.Rectangle;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.TTextBox;
import mrusanov.fantasyruler.localization.Localization;
import mrusanov.fantasyruler.player.Player;

public class MessageSuccessfullySentMessageComponent extends AbstractCreateMessageComponent {

    private final TButton okButton;

    private final TTextBox labelBox;

    public MessageSuccessfullySentMessageComponent(final CreateMessageComponentsContainer parent, Player currentPlayer, Player targetPlayer) {
        super(parent, currentPlayer, targetPlayer);
        okButton = new TButton(Localization.getString("menu.backToList")) {

            @Override
            public void doAction() {
                parent.setComponent(ChooseMessageClassComponent.class);
            }

            ;
        };
        labelBox = new TTextBox(Localization.getString("message.successfullySent"));
        add(okButton);
        add(labelBox);
    }

    @Override
    public void setBounds(Rectangle rect) {
        super.setBounds(rect);
        labelBox.setBounds(getWidth() / 10, getHeight() / 10, 8 * getWidth() / 10, getHeight() / 2);
        okButton.setBounds(getWidth() / 8, 7 * getHeight() / 10, 6 * getWidth() / 8, getHeight() / 5);
    }

    @Override
    public void refreshAndShow() {
    }
}
