package legoass;

import java.util.Queue;
import LightDetection.LightDetectionDaniel;
import lejos.nxt.*;
import turn_off.turn_off_light;
import ultrasonicsensor.UltraSonicSensor;
import utils.*;

/**
 * Klasse zum Bewegen, Orientieren und Suchen im Raum mit Hindernissen 
 * @author Martin Schramm
 * @version 1.00
 */
public class KI {

    private static KI instance = new KI();

    private int x = 25;

    private int y = 25;

    private int s_x = 25;

    private int s_y = 25;

    int searchPoints;

    private int d_x = 0;

    private int d_y = 0;

    private int currentSearchRadius = 0;

    private int[][] room = new int[50][50];

    private int[][] room_visited_points = new int[50][50];

    private Direction lastMovingDirection;

    private utils.NXT_Direction_Queue avoidQueue = new utils.NXT_Direction_Queue();

    private NXT_Navigator navi = new NXT_Navigator(5.3, 10.0, Motor.A, Motor.B);

    /**
	 * Konstruktor f�r Singleton
	 */
    private KI() {
    }

    /**
	 * Singleton, Methode zum Ansprechen der KI
	 * @return	NXT_Navigation_KI Instanz
	 */
    public static KI getInstance() {
        return instance;
    }

    /**
	 * Methode zum Starten der KI
	 */
    public void start() {
        Direction.createDirections();
        UltraSonicSensor.askUltraSonic(0, 45);
        LightDetectionDaniel.isThereALamp();
        lastMovingDirection = Direction.east;
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                room[i][j] = 0;
                room_visited_points[i][j] = 0;
            }
        }
        room[25][25] = 1;
        boolean lightFound = false;
        lightFound = allroundScan();
        room_visited_points[25][25] = 1;
        if (lightFound == false) {
            do {
                searchNextPoint();
                searchRouteToPoint();
                lightFound = allroundScan();
                room_visited_points[x][y] = 1;
                if (lightFound == true) {
                    searchPoints = 0;
                }
            } while (lightFound == false || Button.ESCAPE.isPressed());
        }
        if (Button.ESCAPE.isPressed() == false) {
            Hammer hammer = new Hammer(Motor.C, true);
            hammer.setTouchFree();
            navi.moveForward(360, UltraSonicSensor.getDistance() - 10);
            turn_off_light.turn_off(navi);
        }
    }

    /**
	 * Methode, um den n�chsten Punkt zum Ansteuern zu suchen
	 */
    public void searchNextPoint() {
        boolean newPointFound = false;
        currentSearchRadius = 1;
        s_x = x;
        s_y = y;
        do {
            for (int sx = 25 - currentSearchRadius; sx <= 25 + currentSearchRadius; sx++) {
                for (int sy = 25 - currentSearchRadius; sy <= 25 + currentSearchRadius; sy++) {
                    if (room[sx][sy] == 1 && room_visited_points[sx][sy] == 0 && isPointReachable(sx, sy)) {
                        s_x = sx;
                        s_y = sy;
                        newPointFound = true;
                    }
                }
            }
            currentSearchRadius++;
        } while (newPointFound == false);
    }

    /**
	 * Methode, um den n�chsten Punkt anzusteuern
	 */
    public void searchRouteToPoint() {
        int dx = s_x - x;
        int dy = s_y - y;
        Direction lastDirection = lastMovingDirection;
        lastMovingDirection = null;
        while (dx != 0 || dy != 0) {
            if (avoidQueue.isEmpty() == false && avoidQueue.getFirst() != lastMovingDirection.getAnti() && room[x + avoidQueue.getFirst().getDX()][y + avoidQueue.getFirst().getDY()] == 1) {
                x += avoidQueue.getFirst().getDX();
                y += avoidQueue.getFirst().getDY();
                navi.moveTo((x - 25) * 30, (y - 25) * 30);
                lastMovingDirection = avoidQueue.pop();
            } else {
                if (dx > 0 && room[x + Direction.east.getDX()][y + Direction.east.getDY()] == 1 && lastMovingDirection != Direction.west) {
                    x += Direction.east.getDX();
                    y += Direction.east.getDY();
                    navi.moveTo((x - 25) * 30, (y - 25) * 30);
                    lastMovingDirection = Direction.east;
                } else {
                    if (dx < 0 && room[x + Direction.west.getDX()][y + Direction.west.getDY()] == 1 && lastMovingDirection != Direction.east) {
                        x += Direction.west.getDX();
                        y += Direction.west.getDY();
                        navi.moveTo((x - 25) * 30, (y - 25) * 30);
                        lastMovingDirection = Direction.west;
                    } else {
                        if (dy > 0 && room[x + Direction.north.getDX()][y + Direction.north.getDY()] == 1 && lastMovingDirection != Direction.south) {
                            x += Direction.north.getDX();
                            y += Direction.north.getDY();
                            navi.moveTo((x - 25) * 30, (y - 25) * 30);
                            lastMovingDirection = Direction.north;
                        } else {
                            if (dy < 0 && room[x + Direction.south.getDX()][y + Direction.south.getDY()] == 1 && lastMovingDirection != Direction.north) {
                                x += Direction.south.getDX();
                                y += Direction.south.getDY();
                                navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                lastMovingDirection = Direction.south;
                            } else {
                                if (dx > 0 && room[x + Direction.west.getDX()][y + Direction.west.getDY()] == 1) {
                                    x += Direction.west.getDX();
                                    y += Direction.west.getDY();
                                    navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                    avoidQueue.push(Direction.east);
                                    lastMovingDirection = Direction.west;
                                } else {
                                    if (dx < 0 && room[x + Direction.east.getDX()][y + Direction.east.getDY()] == 1) {
                                        x += Direction.east.getDX();
                                        y += Direction.east.getDY();
                                        navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                        avoidQueue.push(Direction.west);
                                        lastMovingDirection = Direction.east;
                                    } else {
                                        if (dy > 0 && room[x + Direction.south.getDX()][y + Direction.south.getDY()] == 1) {
                                            x += Direction.south.getDX();
                                            y += Direction.south.getDY();
                                            navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                            avoidQueue.push(Direction.north);
                                            lastMovingDirection = Direction.south;
                                        } else {
                                            if (dy < 0 && room[x + Direction.north.getDX()][y + Direction.north.getDY()] == 1) {
                                                x += Direction.north.getDX();
                                                y += Direction.north.getDY();
                                                navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                                avoidQueue.push(Direction.south);
                                                lastMovingDirection = Direction.north;
                                            } else {
                                                if (dx < 0 && room[x + Direction.west.getDX()][y + Direction.west.getDY()] == 1) {
                                                    x += Direction.west.getDX();
                                                    y += Direction.west.getDY();
                                                    navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                                    lastMovingDirection = Direction.west;
                                                }
                                                if (dx > 0 && room[x + Direction.east.getDX()][y + Direction.east.getDY()] == 1) {
                                                    x += Direction.east.getDX();
                                                    y += Direction.east.getDY();
                                                    navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                                    lastMovingDirection = Direction.east;
                                                }
                                                if (dy < 0 && room[x + Direction.south.getDX()][y + Direction.south.getDY()] == 1) {
                                                    x += Direction.south.getDX();
                                                    y += Direction.south.getDY();
                                                    navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                                    lastMovingDirection = Direction.south;
                                                }
                                                if (dy > 0 && room[x + Direction.north.getDX()][y + Direction.north.getDY()] == 1) {
                                                    x += Direction.north.getDX();
                                                    y += Direction.north.getDY();
                                                    navi.moveTo((x - 25) * 30, (y - 25) * 30);
                                                    lastMovingDirection = Direction.north;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            dx = s_x - x;
            dy = s_y - y;
            drawMap();
        }
        if (lastMovingDirection == null) {
            lastMovingDirection = lastDirection;
        }
    }

    /**
	 * Methode um einen Punkt zu scannen
	 */
    private void scanPoint(int x, int y) {
        int status = 1;
        if (UltraSonicSensor.askUltraSonic(0, 40)) {
            status = 2;
            Sound.playTone(440, 500, 100);
        }
        room[x][y] = status;
        drawMap();
    }

    /**
	 * Methode um alle 4 angrenzenden Punkt zu scannen
	 */
    private boolean allroundScan() {
        if (room_visited_points[s_x][s_y] == 0) {
            for (int i = 0; i < 4; i++) {
                if (room[x + lastMovingDirection.getDX()][y + lastMovingDirection.getDY()] == 0) {
                    scanPoint(x + lastMovingDirection.getDX(), y + lastMovingDirection.getDY());
                }
                if (LightDetectionDaniel.isThereALamp()) {
                    Sound.playTone(1000, 500, 100);
                    try {
                        Thread.sleep(750);
                    } catch (Exception e) {
                    }
                    Sound.playTone(1000, 500, 100);
                    try {
                        Thread.sleep(750);
                    } catch (Exception e) {
                    }
                    Sound.playTone(1000, 1000, 100);
                    return true;
                }
                navi.rotate(90);
                lastMovingDirection = lastMovingDirection.getLeft();
            }
        }
        return false;
    }

    /**
	 * Methode zum beginnen
	 */
    public static void main(String args[]) {
        try {
            getInstance().start();
        } catch (Exception e) {
            LCD.clear();
            LCD.drawString("Fehler", 0, 0);
            LCD.drawString(e.toString(), 0, 1);
            LCD.refresh();
            try {
                Thread.sleep(5000);
            } catch (Exception e2) {
            }
        }
    }

    /**
	 * Methode zum Erstellen einer kleinen Karte
	 */
    private void drawMap() {
        LCD.clear();
        LCD.drawString("M", 4, 4);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (room[21 + i][29 - j] == 2) {
                    LCD.drawString("X", i, j);
                }
            }
        }
        LCD.drawString("S", s_x - 21, 8 - (s_y - 21));
        LCD.drawString("A", x - 21, 8 - (y - 21));
        LCD.refresh();
    }

    /**
	 * Methode, die �berpr�ft ob der Punkt erreichbar ist
	 */
    private boolean isPointReachable(int px, int py) {
        if (room[px - 1][py] == 1) {
            return true;
        }
        if (room[px + 1][py] == 1) {
            return true;
        }
        if (room[px][py - 1] == 1) {
            return true;
        }
        if (room[px][py + 1] == 1) {
            return true;
        }
        return false;
    }
}
