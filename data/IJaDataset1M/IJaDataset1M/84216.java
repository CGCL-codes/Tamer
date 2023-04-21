package trb.fps.predict.app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import trb.fps.predict.AnimatedState;
import trb.fps.predict.DelayedInterpolatedState;
import trb.fps.predict.PredictedState;
import trb.fps.predict.TimedInput;
import trb.fps.predict.TimedState;

public class ClientServerApp extends JComponent {

    static final float TRESHOLD = 0.2f;

    static final int RADIUS = 10;

    static final boolean[] keyState = new boolean[0xffff];

    static final Client[] clients = { new Client(0), new Client(1) };

    static final Server server = new Server(clients);

    static boolean aiGoingLeft = false;

    static TimedPosition shooter = new TimedPosition(0l, 0, 0);

    static TimedPosition target = new TimedPosition(0l, 0, 0);

    static final JComponent renderer = new JComponent() {

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            drawPlayer(g, shooter, Color.CYAN);
            drawPlayer(g, target, Color.CYAN);
            for (int i = 0; i < clients.length; i++) {
                Client client = clients[i];
                drawPlayer(g, server.animatedStates[i].newest(), Color.BLUE.darker());
                drawPlayer(g, server.shooterState, Color.PINK);
                drawPlayer(g, server.targetState, Color.PINK);
                g.drawLine((int) server.shooterState.x, (int) server.shooterState.y, (int) server.shooterState.x, (int) server.targetState.y);
                drawPlayer(g, client.interpolatedState[i].getNewestState(), Color.RED.darker());
                drawPlayer(g, client.interpolatedState[i].getCurrentState(), Color.GREEN.darker());
                drawPlayer(g, client.predictedState.getCurrentState(), Color.BLACK);
            }
            g.drawLine(400, 0, 400, getHeight());
        }

        void drawPlayer(Graphics g, TimedPosition state, Color color) {
            g.setColor(color);
            g.fillOval((int) state.x - RADIUS, (int) state.y - RADIUS, RADIUS * 2, RADIUS * 2);
        }
    };

    public static void main(String[] args) {
        renderer.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                keyState[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyState[e.getKeyCode()] = false;
            }
        });
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 640, 480);
        frame.add(renderer);
        frame.setVisible(true);
        renderer.requestFocusInWindow();
        for (final Client client : clients) {
            new Thread(new Runnable() {

                public void run() {
                    client.gameLoop(server);
                }
            }).start();
        }
        server.gameLoop();
    }

    static TimedPositionInput getInput(long now, Client client) {
        if (client == clients[0]) {
            float dx = (keyState[KeyEvent.VK_LEFT] ? -150 : 0) + (keyState[KeyEvent.VK_RIGHT] ? 150 : 0);
            float dy = (keyState[KeyEvent.VK_UP] ? -150 : 0) + (keyState[KeyEvent.VK_DOWN] ? 150 : 0);
            boolean fire = keyState[KeyEvent.VK_SPACE];
            keyState[KeyEvent.VK_SPACE] = false;
            return new TimedPositionInput(now, dx, dy, fire);
        }
        TimedPosition client1state = clients[1].predictedState.getCurrentState();
        if (aiGoingLeft && (client1state.x < 50) || !aiGoingLeft && (client1state.x > 350)) {
            aiGoingLeft = !aiGoingLeft;
        }
        return new TimedPositionInput(now, aiGoingLeft ? -150 : 150, 0, false);
    }

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static class Client {

        private final int index;

        PredictedState<TimedPosition, Updator> predictedState;

        DelayedInterpolatedState<TimedPosition>[] interpolatedState;

        DelayedQueue<TimedPosition[]> in = new DelayedQueue();

        DelayedQueue<PacketToServer> out = new DelayedQueue();

        Client(int index) {
            this.index = index;
            TimedPosition initialState = new TimedPosition(System.currentTimeMillis(), 100, 100);
            predictedState = new PredictedState(initialState);
            interpolatedState = new DelayedInterpolatedState[] { new DelayedInterpolatedState(initialState), new DelayedInterpolatedState(initialState) };
        }

        void gameLoop(Server server) {
            while (true) {
                long now = System.currentTimeMillis();
                TimedPositionInput timedInput = getInput(now, this);
                TimedPosition[] timedStateFromServer = in.remove();
                timedStateFromServer = timedStateFromServer != null ? timedStateFromServer : new TimedPosition[2];
                predictedState.updateAndCorrect(new Updator(timedInput), timedStateFromServer[index]);
                for (int i = 0; i < interpolatedState.length; i++) {
                    interpolatedState[i].update(now, timedStateFromServer[i]);
                }
                out.add(new PacketToServer(timedInput, interpolatedState[0].getCurrentTime(), interpolatedState[1].getCurrentTime()));
                if (timedInput.fire) {
                    shooter = clients[0].predictedState.getCurrentState();
                    target = clients[0].interpolatedState[1].getCurrentState();
                }
                renderer.repaint();
                sleep(16);
            }
        }
    }

    static class PacketToServer {

        TimedPositionInput timedInput;

        long[] clientTimes;

        PacketToServer(TimedPositionInput timedInput, long... clientTimes) {
            this.timedInput = timedInput;
            this.clientTimes = clientTimes;
        }
    }

    static class Server {

        final Client[] clients;

        final AnimatedState<TimedPosition>[] animatedStates;

        TimedPosition shooterState = new TimedPosition(0l, 0, 0);

        TimedPosition targetState = new TimedPosition(0l, 0, 0);

        Server(Client[] clients) {
            this.clients = clients;
            animatedStates = new AnimatedState[] { new AnimatedState(clients[0].predictedState.getCurrentState()), new AnimatedState(clients[1].predictedState.getCurrentState()) };
        }

        void gameLoop() {
            while (true) {
                tick();
                renderer.repaint();
                sleep(100);
            }
        }

        private void tick() {
            for (int clientIdx = 0; clientIdx < clients.length; clientIdx++) {
                while (true) {
                    PacketToServer toServer = clients[clientIdx].out.remove();
                    if (toServer == null) {
                        break;
                    }
                    AnimatedState<TimedPosition> animatedState = animatedStates[clientIdx];
                    TimedPosition newState = animatedState.newest().update(new Updator(toServer.timedInput));
                    animatedState.add(newState);
                    animatedState.removeOlderThan(newState.getTime() - 1000);
                    if (toServer.timedInput.fire) {
                        for (int targetIdx = 0; targetIdx < clients.length; targetIdx++) {
                            if (targetIdx != clientIdx) {
                                headShot(clientIdx, targetIdx, toServer.clientTimes);
                            }
                        }
                    }
                }
            }
            TimedPosition[] toClient = new TimedPosition[animatedStates.length];
            for (int i = 0; i < toClient.length; i++) {
                toClient[i] = animatedStates[i].newest();
            }
            for (int i = 0; i < clients.length; i++) {
                clients[i].in.add(toClient.clone());
            }
        }

        void headShot(int shooterIdx, int targetIdx, long[] clientTimes) {
            shooterState = animatedStates[shooterIdx].newest();
            targetState = getClientStateAtTime(targetIdx, clientTimes[targetIdx]);
            if (Math.abs(targetState.x - shooterState.x) < RADIUS) {
                System.out.println("HIT");
            }
        }

        TimedPosition getClientStateAtTime(int targetIdx, long time) {
            TimedPosition targetTimedState = animatedStates[targetIdx].interpolate(time);
            System.out.println("timeDelta at bullet check " + (targetTimedState.time - time));
            return targetTimedState;
        }
    }

    static class TimedPositionInput implements TimedInput {

        final long time;

        final float dx;

        final float dy;

        final boolean fire;

        TimedPositionInput(long time, float vx, float vy, boolean fire) {
            this.time = time;
            this.dx = vx;
            this.dy = vy;
            this.fire = fire;
        }

        public long getTime() {
            return time;
        }
    }

    static class TimedPosition implements TimedState<TimedPosition, Updator> {

        final long time;

        final float x;

        final float y;

        TimedPosition(long time, float x, float y) {
            this.time = time;
            this.x = x;
            this.y = y;
        }

        public TimedPosition setTime(long time) {
            return new TimedPosition(time, x, y);
        }

        public long getTime() {
            return time;
        }

        public boolean withinPredictThreshold(TimedPosition pos) {
            return distance(pos) < 0.1;
        }

        float distance(TimedPosition s) {
            float dx = s.x - x;
            float dy = s.y - y;
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        public TimedPosition interpolate(float t, TimedPosition s2) {
            return new TimedPosition((long) (time + t * (s2.time - time)), x + t * (s2.x - x), y + t * (s2.y - y));
        }

        public TimedPosition update(Updator updator) {
            TimedPositionInput posInput = updator.input;
            if (posInput.getTime() < time) {
                Thread.dumpStack();
            }
            long timeDeltaMillis = posInput.getTime() - getTime();
            float newx = x + posInput.dx * timeDeltaMillis / 1000f;
            float newy = y + posInput.dy * timeDeltaMillis / 1000f;
            return new TimedPosition(posInput.getTime(), newx, newy);
        }
    }

    static class Updator implements TimedInput {

        TimedPositionInput input;

        public Updator(TimedPositionInput input) {
            this.input = input;
        }

        public long getTime() {
            return input.time;
        }
    }
}
