package net.sf.groofy.player.webplayer;

import net.sf.groofy.datamodel.Correspondence;
import net.sf.groofy.i18n.Messages;
import net.sf.groofy.player.AbstractUiPlayer;
import net.sf.groofy.player.IPlayer;
import net.sf.groofy.preferences.GroofyConstants;
import net.sf.groofy.util.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class WebBasedGroovesharkPlayer extends AbstractUiPlayer implements IPlayer {

    private Browser playerBrowser;

    private Button optionsButton = null;

    public WebBasedGroovesharkPlayer(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    @Override
    public void destroy() {
        stop();
        if (playerBrowser != null) {
            playerBrowser.dispose();
        }
    }

    public void initialize() {
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.heightHint = 14;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = GridData.CENTER;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.heightHint = 40;
        gridData.widthHint = 200;
        gridData.verticalAlignment = GridData.BEGINNING;
        GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        this.setLayout(gridLayout);
        if (isPlatformSupported()) {
            playerBrowser = new Browser(this, SWT.NONE);
            playerBrowser.setUrl(GroofyConstants.EMPTY_PLAYER_URL);
            playerBrowser.setLayoutData(gridData);
            optionsButton = new Button(this, SWT.FLAT);
            optionsButton.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream(GroofyConstants.PATH_OTHER_DOWN)));
            optionsButton.setLayoutData(gridData1);
        } else {
            GridData gridData11 = new GridData();
            gridData11.horizontalAlignment = GridData.FILL;
            gridData11.verticalAlignment = GridData.CENTER;
            gridData11.grabExcessHorizontalSpace = true;
            gridData11.grabExcessVerticalSpace = true;
            Composite composite = new Composite(this, SWT.NONE);
            composite.setLayout(new GridLayout());
            composite.setLayoutData(gridData11);
            Label label = new Label(composite, SWT.CENTER);
            label.setText(Messages.getString("WebBasedGroovesharkPlayer.UnsupportedPlatform"));
            label.setLayoutData(gridData11);
        }
    }

    @Override
    public void playGroovesharkSong(Correspondence groovesharkTrack) {
        if (isPlatformSupported() && !playerBrowser.isDisposed()) {
            playerBrowser.setUrl(String.format(GroofyConstants.PLAYER_URL_SONG, groovesharkTrack.getSongID()));
        }
    }

    @Override
    public void stop() {
        if (isPlatformSupported() && !playerBrowser.isDisposed()) {
            playerBrowser.setUrl(GroofyConstants.EMPTY_PLAYER_URL);
        }
    }

    private boolean isPlatformSupported() {
        return Platform.isWindows();
    }
}
