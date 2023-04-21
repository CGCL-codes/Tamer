package gamestates;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import loader.LoaderThread;
import main.Main;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;

public class LoadingGameState extends com.jmex.game.state.load.LoadingGameState {

    private AudioTrack music;

    private URL tableResource;

    private URL tableTextureResource;

    private LoadWorker loadWorker;

    private InputHandler input;

    public LoadingGameState(PinballGameStateSettings settings, URL tableResource, URL tableTextureResources) {
        this.tableResource = tableResource;
        this.tableTextureResource = tableTextureResources;
        music = Main.getAudioSystem().createAudioTrack(this.getClass().getClassLoader().getResource("resources/sounds/loading/music.wav"), false);
        music.setType(TrackType.MUSIC);
        music.setLooping(true);
        music.setTargetVolume(Main.getMusicVolume());
        input = new InputHandler();
        input.addAction(new InputAction() {

            public void performAction(InputActionEvent evt) {
                if (evt.getTriggerPressed()) {
                    loadWorker.stopLoading();
                    setProgress(0, "Aborting");
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_ESCAPE, InputHandler.AXIS_NONE, false);
        input.addAction(new InputAction() {

            public void performAction(InputActionEvent evt) {
                if (evt.getTriggerPressed()) {
                    Main.toggleMuteAudio();
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_O, InputHandler.AXIS_NONE, false);
        final PinballGameState pinballGS = Main.newPinballGame(settings);
        loadWorker = new LoadWorker(pinballGS, this);
        setProgress(0, "Loading...");
    }

    public void startLoading() {
    }

    private void endLoad() {
        Main.endLoading();
        if (loadWorker.aborted) {
            Main.newMenu().setActive(true);
        } else loadWorker.endWork();
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            MouseInput.get().setCursorVisible(true);
            Thread t = new Thread(loadWorker, "LoadingThread");
            t.start();
            Main.getAudioSystem().getMusicQueue().addTrack(music);
            Main.getAudioSystem().getMusicQueue().setCurrentTrack(music);
        } else {
            if (loadWorker.isCompleted()) {
                if (!loadWorker.aborted) MouseInput.get().setCursorVisible(false);
                endLoad();
            }
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        input.update(tpf);
        Main.getAudioSystem().update();
    }

    public void cleanup() {
        rootNode.detachAllChildren();
        rootNode.clearControllers();
    }

    private class LoadWorker implements Runnable, Callable<Void>, Observer {

        private PinballGameState pinballGS;

        private LoadingGameState loadingGS;

        private LoaderThread roomLoader, machineLoader, tableLoader;

        private volatile boolean complete = false, aborted = false;

        private volatile float percentageMachine = 0f, percentageRoom = 0f, percentageTable = 0f;

        public LoadWorker(PinballGameState pinballGS, LoadingGameState loadingGS) {
            super();
            this.pinballGS = pinballGS;
            this.loadingGS = loadingGS;
        }

        public synchronized boolean isCompleted() {
            return this.complete;
        }

        public float getPercentage() {
            return (percentageRoom + percentageMachine + percentageTable) / 3;
        }

        public void endWork() {
            TextureManager.preloadCache(DisplaySystem.getDisplaySystem().getRenderer());
            pinballGS.initGame();
            pinballGS.setActive(true);
        }

        public void run() {
            roomLoader = new LoaderThread(LoadingGameState.class.getClassLoader().getResource("resources/models/Room.x3d"), pinballGS, 0, null);
            roomLoader.addObserver(this);
            Thread roomThread = new Thread(roomLoader, "roomLoadThread");
            machineLoader = new LoaderThread(LoadingGameState.class.getClassLoader().getResource("resources/models/Machine.x3d"), pinballGS, 1, tableTextureResource);
            machineLoader.addObserver(this);
            Thread machineThread = new Thread(machineLoader, "machineLoadThread");
            tableLoader = new LoaderThread(tableResource, pinballGS, 2, tableTextureResource);
            tableLoader.addObserver(this);
            Thread tableThread = new Thread(tableLoader, "tableLoadThread");
            if (!aborted) {
                roomThread.start();
                machineThread.start();
                tableThread.start();
                try {
                    roomThread.join();
                    machineThread.join();
                    tableThread.join();
                } catch (InterruptedException e) {
                }
            }
            if (!aborted) {
                pinballGS.getRootNode().attachChild(roomLoader.getScene());
                pinballGS.getRootNode().attachChild(machineLoader.getScene());
                pinballGS.getRootNode().attachChild(pinballGS.inclinePinball(tableLoader.getScene()));
                pinballGS.setGameLogic(tableLoader.getTheme());
                pinballGS.setLoadingComplete(true);
            }
            this.complete = true;
            this.loadingGS.setProgress(1);
        }

        public Void call() throws Exception {
            if (!complete) this.loadingGS.setProgress(getPercentage());
            return null;
        }

        public void stopLoading() {
            roomLoader.stop();
            machineLoader.stop();
            tableLoader.stop();
            aborted = true;
        }

        public void update(Observable o, Object arg) {
            if (o instanceof LoaderThread) {
                LoaderThread lt = (LoaderThread) o;
                int id = lt.getID();
                if (id == 0) percentageRoom = lt.getPercentComplete(); else if (id == 1) percentageMachine = lt.getPercentComplete(); else if (id == 2) percentageTable = lt.getPercentComplete();
            }
            if (!complete && !aborted) {
                GameTaskQueueManager.getManager().update(this);
            }
        }
    }
}
