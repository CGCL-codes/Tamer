package net.sf.doolin.app.sc.client.view;

import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import net.sf.doolin.app.sc.client.bean.GameBean;
import net.sf.doolin.app.sc.client.bean.GameGeom;
import net.sf.doolin.app.sc.client.bus.ToolStatusMessage;
import net.sf.doolin.app.sc.client.model.LocalPlayerModel;
import net.sf.doolin.app.sc.game.support.FormatUtils;
import net.sf.doolin.app.sc.game.type.DisplayMass;
import net.sf.doolin.app.sc.game.type.DisplayTemp;
import net.sf.doolin.app.sc.game.type.Mass;
import net.sf.doolin.app.sc.game.type.Temp;
import net.sf.doolin.bus.Bus;
import net.sf.doolin.bus.support.AbstractClassSubscriber;
import net.sf.doolin.bus.support.PropertyChangeSupport;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.action.path.item.AbstractActionPath;
import net.sf.doolin.gui.action.swing.ActionFactory;
import net.sf.doolin.gui.action.swing.MenuBuilder;
import net.sf.doolin.gui.util.GUIStrings;
import org.apache.commons.lang.StringUtils;

public class GameStatusBar extends AbstractActionPath {

    protected JLabel createGameBox(ActionContext actionContext, final GameBean model, Border border) {
        final JLabel labelGame = new JLabel();
        labelGame.setBorder(border);
        Runnable updateGameTask = new Runnable() {

            @Override
            public void run() {
                String name = model.getName();
                name = StringUtils.isBlank(name) ? " " : name;
                labelGame.setText(GUIStrings.get("StatusBar.game", name));
            }
        };
        PropertyChangeSupport.subscribe(actionContext.getSubscriberValidator(), model, GameBean.NAME, updateGameTask);
        updateGameTask.run();
        return labelGame;
    }

    protected JLabel createPlayerBox(ActionContext actionContext, GameBean model, Border border) {
        LocalPlayerModel localPlayer = model.getLocalPlayerModel();
        Mass mass = DisplayMass.REFERENCE;
        Temp temp = DisplayTemp.REFERENCE;
        String text = GUIStrings.get("LocalPlayer.reference", localPlayer.getName(), FormatUtils.formatMass(null, mass), FormatUtils.formatTemperature(null, temp));
        JLabel labelPlayer = new JLabel();
        labelPlayer.setBorder(border);
        labelPlayer.setText(text);
        return labelPlayer;
    }

    protected JLabel createToolMessageBox(ActionContext actionContext, GameBean model, Border border) {
        final JLabel label = new JLabel();
        label.setBorder(border);
        Bus.get().subscribe(new AbstractClassSubscriber<ToolStatusMessage>(ToolStatusMessage.class, actionContext.getSubscriberValidator()) {

            @Override
            public String getExecutionDescription() {
                return "Update the tool message status box";
            }

            @Override
            public void receive(ToolStatusMessage message) {
                String text = message.getMessage();
                if (StringUtils.isBlank(text)) {
                    text = " ";
                }
                label.setText(text);
            }
        });
        label.setText(" ");
        return label;
    }

    protected JLabel createTurnBox(ActionContext actionContext, final GameBean model, Border border) {
        final JLabel labelTurn = new JLabel();
        labelTurn.setBorder(border);
        Runnable updateTurnTask = new Runnable() {

            @Override
            public void run() {
                final int year = model.getYear();
                labelTurn.setText(GUIStrings.get("GameTurn.year", String.format("%,d", year)));
            }
        };
        PropertyChangeSupport.subscribe(actionContext.getSubscriberValidator(), model, GameBean.YEAR, updateTurnTask);
        updateTurnTask.run();
        return labelTurn;
    }

    protected JLabel createZoomBox(ActionContext actionContext, final GameBean model, Border border) {
        final JLabel labelZoom = new JLabel();
        labelZoom.setBorder(border);
        Runnable updateZoomTask = new Runnable() {

            @Override
            public void run() {
                BigDecimal zoom = model.getViewSettings().getGeom().getZoom();
                if (zoom == null) {
                    zoom = BigDecimal.ONE;
                }
                labelZoom.setText(GUIStrings.get("StatusBar.zoom", String.format("%,.0f", zoom.multiply(new BigDecimal(100)))));
            }
        };
        PropertyChangeSupport.subscribe(actionContext.getSubscriberValidator(), model.getViewSettings().getGeom(), GameGeom.ZOOM, updateZoomTask);
        updateZoomTask.run();
        return labelZoom;
    }

    @Override
    public void install(MenuBuilder menuBuilder, ActionFactory actionFactory, ActionContext actionContext) {
        final GameBean model = (GameBean) actionContext.getData();
        Border border = BorderFactory.createLoweredBevelBorder();
        border = BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(2, 2, 2, 2));
        JLabel labelGame = createGameBox(actionContext, model, border);
        JLabel labelZoom = createZoomBox(actionContext, model, border);
        JLabel labelPlayer = createPlayerBox(actionContext, model, border);
        JLabel labelTurn = createTurnBox(actionContext, model, border);
        JLabel labelToolMessage = createToolMessageBox(actionContext, model, border);
        menuBuilder.add(labelGame);
        menuBuilder.add(labelPlayer);
        menuBuilder.add(labelTurn);
        menuBuilder.add(labelZoom);
        menuBuilder.add(labelToolMessage);
    }
}
