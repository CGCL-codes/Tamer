package starcraft.gameclient.slick;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;
import starcraft.gameclient.rcp.StarCraftClientRCP;

public class SlickGameViewPart extends ViewPart {

    public static final String ID = "starcraft.gameclient.opengl.SlickGameView";

    public SlickGameViewPart() {
    }

    @Override
    public void createPartControl(Composite parent) {
        boolean useSWT = false;
        if (useSWT) {
            createSWTCanvas(parent);
        } else {
            createAWTCanvas(parent);
        }
    }

    @Override
    public void setFocus() {
    }

    private void createSWTCanvas(Composite parent) {
        try {
            GLData glData = new GLData();
            glData.doubleBuffer = true;
            SWTGameContainer canvasGameContainer = new SWTGameContainer(parent, glData, StarCraftClientRCP.getSlickGame());
            canvasGameContainer.getContainer().setVSync(true);
            canvasGameContainer.getContainer().setSmoothDeltas(false);
            canvasGameContainer.getContainer().setMinimumLogicUpdateInterval(50);
            canvasGameContainer.getContainer().setAlwaysRender(true);
            canvasGameContainer.start();
        } catch (SlickException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Label errorLabel = new Label(parent, SWT.NONE);
            errorLabel.setText(sw.toString());
            e.printStackTrace();
        }
    }

    private void createAWTCanvas(Composite parent) {
        try {
            CanvasGameContainer canvasGameContainer = new CanvasGameContainer(StarCraftClientRCP.getSlickGame());
            canvasGameContainer.getContainer().setVSync(true);
            canvasGameContainer.getContainer().setTargetFrameRate(60);
            canvasGameContainer.getContainer().setSmoothDeltas(false);
            canvasGameContainer.getContainer().setMinimumLogicUpdateInterval(50);
            Composite containerComp = new Composite(parent, SWT.EMBEDDED);
            containerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            java.awt.Frame containerFrame = SWT_AWT.new_Frame(containerComp);
            containerFrame.add(canvasGameContainer);
            canvasGameContainer.getContainer().setAlwaysRender(true);
            canvasGameContainer.requestFocus();
            canvasGameContainer.start();
        } catch (SlickException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Label errorLabel = new Label(parent, SWT.NONE);
            errorLabel.setText(sw.toString());
            e.printStackTrace();
        }
    }
}
