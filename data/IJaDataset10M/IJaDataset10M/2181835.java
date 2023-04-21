package com.uglygreencar.pong;

import com.threerings.crowd.client.PlaceView;
import com.threerings.crowd.data.PlaceObject;
import com.threerings.crowd.util.CrowdContext;
import com.threerings.parlor.game.client.GameController;
import com.threerings.presents.dobj.AttributeChangeListener;
import com.threerings.presents.dobj.AttributeChangedEvent;
import com.threerings.toybox.util.ToyBoxContext;
import com.threerings.util.Name;
import com.uglygreencar.pong.server.objects.PaddleObject;
import com.uglygreencar.pong.server.objects.PongObject;
import com.uglygreencar.pong.sprites.PaddleSprite;

/**
 * Manages the client side mechanics of the game.
 */
public class PongController extends GameController implements AttributeChangeListener {

    protected PongPanel pongPanel;

    protected PongObject pongObject;

    private PongKeyListener pongKeyListener;

    private PaddleSprite paddlePlayerOne;

    private PaddleSprite paddlePlayerTwo;

    private int player;

    private Name playerName;

    /**
	 * Requests that we leave the game and return to the lobby.
	 */
    public void backToLobby() {
        _ctx.getLocationDirector().moveBack();
    }

    @Override
    public void willEnterPlace(PlaceObject placeObject) {
        super.willEnterPlace(placeObject);
        pongObject = (PongObject) placeObject;
        this.player = this.pongObject.getPlayerIndex(this.playerName);
        this.paddlePlayerOne = new PaddleSprite(pongObject.paddleObjectPlayerOne);
        this.pongPanel._bview.addSprite(this.paddlePlayerOne);
        this.paddlePlayerTwo = new PaddleSprite(pongObject.paddleObjectPlayerTwo);
        this.pongPanel._bview.addSprite(this.paddlePlayerTwo);
    }

    /************************************************************************
	 * 
	 * @param player
	 * @param x
	 ************************************************************************/
    public void translatePaddle(int x) {
        if (this.player == 0) {
            this.pongObject.paddleObjectPlayerOne.x += x;
            this.paddlePlayerOne.updatePaddle(this.pongObject.paddleObjectPlayerOne);
            System.out.println("........manager.invoke : 0");
            this.pongObject.manager.invoke("translatePaddle", this.pongObject.paddleObjectPlayerOne);
        } else {
            this.pongObject.paddleObjectPlayerTwo.x += x;
            this.paddlePlayerTwo.updatePaddle(this.pongObject.paddleObjectPlayerTwo);
            System.out.println("........manager.invoke : 1");
            this.pongObject.manager.invoke("translatePaddle", this.pongObject.paddleObjectPlayerTwo);
        }
    }

    /****************************************************************************
	 * 
	 ****************************************************************************/
    @Override
    public void attributeChanged(AttributeChangedEvent attributeChangedEvent) {
        System.out.println("........changed event: " + attributeChangedEvent.getName());
        if (attributeChangedEvent.getValue() instanceof PaddleObject) {
            PaddleObject paddleObject = (PaddleObject) attributeChangedEvent.getValue();
            if (paddleObject.id != this.player) {
                this.paddlePlayerTwo.updatePaddle(paddleObject);
            }
        }
    }

    @Override
    public void didLeavePlace(PlaceObject plobj) {
        super.didLeavePlace(plobj);
        this.pongObject = null;
    }

    @Override
    protected PlaceView createPlaceView(CrowdContext ctx) {
        ToyBoxContext toyBoxContext = (ToyBoxContext) ctx;
        this.pongPanel = new PongPanel(toyBoxContext, this);
        this.playerName = toyBoxContext.getUsername();
        this.pongKeyListener = new PongKeyListener(this);
        toyBoxContext.getKeyDispatcher().addGlobalKeyListener(this.pongKeyListener);
        return this.pongPanel;
    }

    @Override
    protected void gameDidStart() {
        super.gameDidStart();
    }

    @Override
    protected void gameDidEnd() {
        super.gameDidEnd();
    }
}
