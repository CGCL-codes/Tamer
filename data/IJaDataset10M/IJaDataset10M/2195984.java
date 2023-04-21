package coolkey.defender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import coolkey.CoolKey;

/**
 * Klasa mechanizmu animacji gry.
 */
public class Engine implements Runnable {

    /**
	 * Stała zdefiniowana jako stan wyświetlenia menu gry.
	 */
    public static final int STATE_MENU = 0;

    /**
	 * Stała zdefiniowana jako stan wyświetlenia gry.
	 */
    public static final int STATE_GAME = 1;

    /**
	 * Stała zdefiniowana jako stan wyświetlenia wyniku gry.
	 */
    public static final int STATE_RESULT = 2;

    /**
	 * Stała zdefiniowana jako stan wyświetlenia listy najlepszych wyników.
	 */
    public static final int STATE_TOP10 = 3;

    /**
	 * Stała zdefiniowana jako stan wyświetlenia pomocy.
	 */
    public static final int STATE_HELP = 4;

    /**
	 * Stała zdefiniowana jako identyfikator przycisku 'Kontynuj'
	 */
    public static final int MENU_CONTINUE = 0;

    /**
	 * Stała zdefiniowana jako identyfikator przycisku 'Nowa gra'
	 */
    public static final int MENU_NEW = 1;

    /**
	 * Stała zdefiniowana jako identyfikator przycisku 'Top 10'
	 */
    public static final int MENU_TOP10 = 2;

    /**
	 * Stała zdefiniowana jako identyfikator przycisku 'Pomoc'
	 */
    public static final int MENU_HELP = 3;

    /**
	 * Stała zdefiniowana jako identyfikator przycisku powrotu do menu.
	 */
    public static final int MENU_ESC = 4;

    /**
	 * Stała zdefiniowana jako szerokość obszaru gry.
	 */
    public static final int WIDTH = 640;

    /**
	 * Stała zdefiniowana jako wysokość obszaru gry.
	 */
    public static final int HEIGHT = 480;

    /**
	 * Stała zdefiniowana jako maksymalna liczba żyć.
	 */
    public static final int GAME_LIFE_MAX = 5;

    /**
	 * Stała zdefiniowana jako maksymalna liczba poziomów.
	 */
    public static final int GAME_LEVEL_MAX = 21;

    /**
	 * Stała zdefiniowana jako granica po której eksploduje bomba.
	 */
    public static final int GAME_GROUND = 416;

    /**
	 * Stała zdefiniowana jako szybkość animacji wybuchu.
	 */
    public static final double GAME_EXPLOSION_SPEED = 10.0;

    /**
	 * Stała zdefiniowana jako maksymalna liczba wyników w 'Top 10'
	 */
    public static final int TOP10_RESULTS = 10;

    /**
	 * Stała zdefiniowana jako czas przerwy w animacji.
	 */
    private final int INTERVAL = 40;

    /**
	 * Uchwyt do obszaru, na którym jest rysowana gra.
	 */
    private final Canvas container;

    /**
	 * Uchwyt do urządzenia, które wyświetla obraz.
	 */
    private final Display display;

    /**
	 * Liczba klatek na sekunte.
	 */
    private double fps;

    /**
	 * Czy pokazać liczbę klatek na sekunde.
	 */
    private boolean showFps;

    /**
	 * Służy do ustalenia liczby klatek na sekunde.
	 */
    private long timeLast;

    /**
	 * Zlicza ilość klatek.
	 */
    private int countFrame;

    /**
	 * Czy jest nową gra.
	 */
    private boolean newGame;

    /**
	 * Status gry.
	 */
    private int state;

    /**
	 * Identyfikator przycisku, który jest zaznaczony.
	 */
    private int menuSelectId;

    /**
	 * Czy przycisk powrotu do menu jest zaznaczony.
	 */
    private boolean menuEscSelect;

    /**
	 * Lista rozmiarów i pozycji poszczególnych przycisków.
	 */
    private List<Rectangle> menu;

    /**
	 * Poziom aktualnej gry.
	 */
    private int gameLevel;

    /**
	 * Liczba żyć aktualnej gry.
	 */
    private int gameLife;

    /**
	 * Liczba punktów aktualnej gry.
	 */
    private int gameScore;

    /**
	 * Czas trwania aktualnej gry.
	 */
    private long gameTime;

    /**
	 * Aktualnie wpisane słowo.
	 */
    private String gameWord;

    /**
	 * Czy słowo ma być wyczyszczone.
	 */
    private boolean gameWordClear;

    /**
	 * Mapa wszystkich bomb.
	 */
    private Map<String, Bomb> gameBombs;

    /**
	 * Lista bomb, które eksplodowały.
	 */
    private List<Bomb> gameBombsExplosion;

    /**
	 * Uchwyt do stopera.
	 */
    private final Timer gameTimer;

    /**
	 * Tworzy nowy obiekt mechanizmu gry.
	 * @param container obszar na którym ma być rysowana gra.
	 */
    public Engine(Canvas container) {
        this.container = container;
        this.display = this.container.getDisplay();
        this.fps = 25.0;
        this.showFps = false;
        this.newGame = true;
        this.state = STATE_MENU;
        this.menuSelectId = MENU_NEW;
        this.menuEscSelect = false;
        this.menu = new ArrayList<Rectangle>();
        this.menu.add(MENU_CONTINUE, new Rectangle(192, 150, 256, 64));
        this.menu.add(MENU_NEW, new Rectangle(192, 214, 256, 64));
        this.menu.add(MENU_TOP10, new Rectangle(192, 278, 256, 64));
        this.menu.add(MENU_HELP, new Rectangle(192, 342, 256, 64));
        this.menu.add(MENU_ESC, new Rectangle(592, 432, 32, 32));
        this.gameTimer = new Timer("gameTimer");
        this.gameNew();
    }

    /**
	 * Zwraca liczbę klatek na sekunde.
	 * @return liczba klatek na sekunde
	 */
    public synchronized double getFps() {
        return this.fps;
    }

    /**
	 * Czy liczba klatek na sekunde ma być wyświetlona na ekranie.
	 * @return true - wyświetl, false - nie wyświetlaj
	 */
    public synchronized boolean showFps() {
        return this.showFps;
    }

    /**
	 * Ustawia czy liczba klatek na sekunde ma być wyświetlona. 
	 * @param showFps true - wyświetl, false - nie wyświetlaj
	 */
    public synchronized void showFps(boolean showFps) {
        this.showFps = showFps;
    }

    /**
	 * Zwraca status gry.
	 * @return status gry
	 */
    public synchronized int getState() {
        return this.state;
    }

    /**
	 * Sprawdza czy obecnie jest nową grą.
	 * @return true - nowa gra, false w przeciwnym wypadku
	 */
    public synchronized boolean isNewGame() {
        return this.newGame;
    }

    /**
	 * Zwraca identyfikator zaznaczonego przycisku.
	 * @return identyfikator przycisku
	 */
    public synchronized int getMenuSelectId() {
        return this.menuSelectId;
    }

    /**
	 * Sprawdza czy przycisk powrotu do menu jest zaznaczony.
	 * @return true gdy jest zaznaczony, false w przeciwnym wypadku
	 */
    public synchronized boolean isMenuEscSelect() {
        return this.menuEscSelect;
    }

    /**
	 * Zwraca wymiary i pozycję przycisko o podanym identyfikatorze.
	 * @param id identyfikator przycisku
	 * @return wymiary i pozycja przycisku
	 */
    public synchronized Rectangle getMenu(int id) {
        return this.menu.get(id);
    }

    /**
	 * Zmienia identyfikator zaznaczonego przycisku na następny.
	 */
    public synchronized void keyNext() {
        switch(this.menuSelectId) {
            case MENU_CONTINUE:
            case MENU_NEW:
            case MENU_TOP10:
                break;
            case MENU_HELP:
                return;
            default:
                this.menuSelectId = MENU_HELP;
                return;
        }
        this.menuSelectId++;
    }

    /**
	 * Zmienia identyfikator zaznaczonego przycisku na poprzedni.
	 */
    public synchronized void keyPrev() {
        switch(this.menuSelectId) {
            case MENU_CONTINUE:
                return;
            case MENU_NEW:
                if (this.newGame) return;
            case MENU_TOP10:
            case MENU_HELP:
                break;
            default:
                if (this.newGame) this.menuSelectId = MENU_NEW; else this.menuSelectId = MENU_CONTINUE;
                return;
        }
        this.menuSelectId--;
    }

    /**
	 * Zmienia status gry odpowiednio dla zaznaczonego identyfikatora przycisku
	 */
    public synchronized void keyEnter() {
        switch(this.menuSelectId) {
            case MENU_CONTINUE:
                if (!this.newGame) this.state = STATE_GAME;
                break;
            case MENU_NEW:
                this.gameNew();
                this.newGame = false;
                this.state = STATE_GAME;
                break;
            case MENU_TOP10:
                this.state = STATE_TOP10;
                break;
            case MENU_HELP:
                this.state = STATE_HELP;
                break;
        }
    }

    /**
	 * Zmenia status gry na powrót do menu.
	 */
    public synchronized void keyEsc() {
        switch(this.state) {
            case STATE_GAME:
                if (this.newGame) this.menuSelectId = MENU_NEW; else this.menuSelectId = MENU_CONTINUE;
                break;
            case STATE_RESULT:
                this.menuSelectId = MENU_NEW;
                break;
        }
        this.state = STATE_MENU;
    }

    /**
	 * Zmienia status gry odpowiednio dla kliknięcia lewego przycisku myszy.
	 * @param x współrzędna kursora na osi x
	 * @param y współrzędna kursora na osi y
	 */
    public synchronized void mouseUp(int x, int y) {
        switch(this.state) {
            case STATE_MENU:
                Rectangle size = this.menu.get(MENU_CONTINUE);
                if (size.x > x || size.x + size.width < x) return;
                int select = (y - size.y) / size.height;
                if (select != this.menuSelectId) return;
                switch(select) {
                    case MENU_CONTINUE:
                        if (!this.newGame) this.state = STATE_GAME;
                        break;
                    case MENU_NEW:
                        this.gameNew();
                        this.newGame = false;
                        this.state = STATE_GAME;
                        break;
                    case MENU_TOP10:
                        this.state = STATE_TOP10;
                        break;
                    case MENU_HELP:
                        this.state = STATE_HELP;
                        break;
                }
                break;
            case STATE_GAME:
            case STATE_RESULT:
            case STATE_TOP10:
            case STATE_HELP:
                Rectangle escSize = this.menu.get(MENU_ESC);
                if (escSize.x <= x && escSize.x + escSize.width >= x && escSize.y <= y && escSize.y + escSize.height >= y) {
                    this.menuEscSelect = false;
                    if (this.state == STATE_RESULT) this.menuSelectId = MENU_NEW;
                    this.state = STATE_MENU;
                }
                break;
        }
    }

    /**
	 * Zmienia status gry na powrót do menu dla kliknięcia prawego przycisku myszy.
	 */
    public synchronized void mouseUpEsc() {
        if (this.state == STATE_RESULT) this.menuSelectId = MENU_NEW;
        this.state = STATE_MENU;
    }

    /**
	 * Zmienia idetyfikator zaznaczonego przycisku odpowiednio dla podanych współrzędnych kursora
	 * @param x współrzędna kursora na osi x
	 * @param y współrzędna kursora na osi y
	 */
    public synchronized void mouseMove(int x, int y) {
        switch(this.state) {
            case STATE_MENU:
                Rectangle menuSize = this.menu.get(MENU_CONTINUE);
                if (menuSize.x > x || menuSize.x + menuSize.width < x) return;
                switch((y - menuSize.y) / menuSize.height) {
                    case MENU_CONTINUE:
                        if (!this.newGame) this.menuSelectId = MENU_CONTINUE;
                        break;
                    case MENU_NEW:
                        this.menuSelectId = MENU_NEW;
                        break;
                    case MENU_TOP10:
                        this.menuSelectId = MENU_TOP10;
                        break;
                    case MENU_HELP:
                        this.menuSelectId = MENU_HELP;
                        break;
                }
                break;
            case STATE_GAME:
            case STATE_RESULT:
            case STATE_TOP10:
            case STATE_HELP:
                Rectangle escSize = this.menu.get(MENU_ESC);
                if (escSize.x <= x && escSize.x + escSize.width >= x && escSize.y <= y && escSize.y + escSize.height >= y) this.menuEscSelect = true; else this.menuEscSelect = false;
                break;
        }
    }

    /**
	 * Zwraca liczbę aktualnych żyć.
	 * @return liczbę aktualnych żyć
	 */
    public synchronized int gameGetLife() {
        return this.gameLife;
    }

    /**
	 * Zwraca aktualny poziom gry.
	 * @return poziom gry
	 */
    public synchronized int gameGetLevel() {
        return this.gameLevel;
    }

    /**
	 * Zwraca aktualną liczbę zdobytych punktów.
	 * @return liczba punktów
	 */
    public synchronized int gameGetScore() {
        return this.gameScore;
    }

    /**
	 * Zwraca aktulny czas trwania gry.
	 * @return czas trwania gry
	 */
    public synchronized long gameGetTime() {
        return this.gameTime;
    }

    /**
	 * Zwraca aktualnie wpisane słowo.
	 * @return wpisane słowo
	 */
    public synchronized String gameGetWord() {
        return this.gameWord;
    }

    /**
	 * Sprawdza czy słowo jest wyczyszczone.
	 * @return true gdy słowo jest wyczyszczone, false w przeciwnym wypadku
	 */
    public synchronized boolean gameIsWordClear() {
        return this.gameWordClear;
    }

    /**
	 * Zwraca kolekcje wszystich bomb.
	 * @return kolekcja bomb
	 */
    public synchronized Collection<Bomb> gameGetBombs() {
        return this.gameBombs.values();
    }

    /**
	 * Zwraca list bomb, które eksplodowały.
	 * @return lista eksplodujących bomb
	 */
    public synchronized List<Bomb> gameGetBombsExplosion() {
        return this.gameBombsExplosion;
    }

    /**
	 * Zwraca maksymalną liczbę bomb, które mogą być w grze w tym samym czasie. Zależy od poziomu gry. 
	 * @return maksymalną liczbę bomb
	 */
    private synchronized int gameHowBombs() {
        return this.gameLevel + 2;
    }

    /**
	 * Zwraca jak szybko spada bomba odpowiednio dla poziomu gry. 
	 * @return szydkość spadania bomby
	 */
    private double gameHowFast() {
        return this.gameLevel * 1.5 + 14.0;
    }

    /**
	 * Zwraca liczbę punktów, które trzeba zdobyć by przejść na kolejny poziom gry.
	 * @return liczbę punktów, które trzeba zdobyć
	 */
    private synchronized int gameHowScore() {
        return (int) Math.pow(this.gameLevel, 2.0) + 19 * this.gameLevel - 5;
    }

    /**
	 * Dodaje do końca słowa podany znak.
	 * @param c znak, który zostanie dodany
	 */
    public synchronized void gameWordAdd(char c) {
        String specialChar = ",./;\'[]<>?:\"{}!@#$%^&*()_+|\\-=";
        if (Character.isLetterOrDigit(c) || specialChar.indexOf(c) != -1) {
            if (this.gameWordClear) {
                this.gameWord = "";
                this.gameWordClear = false;
            }
            this.gameWord = this.gameWord + c;
        }
        this.gameWordEnter();
    }

    /**
	 * Usuwa ostatni znak ze słowa.
	 */
    public synchronized void gameWordDel() {
        if (this.gameWordClear) {
            this.gameWord = "";
            this.gameWordClear = false;
        }
        if (this.gameWord.length() > 0) this.gameWord = this.gameWord.substring(0, this.gameWord.length() - 1);
        this.gameWordEnter();
    }

    /**
	 * Usuwa bombę, która ma etykietę taką samą jak wpisane słowo oraz zwiększa liczbę punktów o 1.
	 */
    private synchronized void gameWordEnter() {
        if (this.gameBombs.containsKey(this.gameWord)) {
            this.gameBombsExplosion.add(this.gameBombs.get(this.gameWord));
            this.gameSoundExplosion2();
            this.gameBombs.remove(this.gameWord);
            this.gameScore++;
            this.gameWordClear = true;
        }
    }

    /**
	 * Ustawia wszystkie parametry gry na początkowe.
	 */
    private synchronized void gameNew() {
        this.gameLife = GAME_LIFE_MAX;
        this.gameScore = 0;
        this.gameTime = 0;
        this.gameLevel = 1;
        this.gameWord = "";
        this.gameWordClear = false;
        this.gameBombs = new HashMap<String, Bomb>();
        this.gameBombsExplosion = new ArrayList<Bomb>();
    }

    /**
	 * Uzupełnia liczbę bomb do maksymalnej ilości, które mogą być w jednej chwili.
	 */
    private synchronized void gameCreateBombs() {
        Thread gameCreateBombsThread = new Thread(new Runnable() {

            public void run() {
                Random random = new Random();
                Image imageTmp = new Image(display, 1, 1);
                GC gc = new GC(imageTmp);
                while (gameBombs.size() < gameHowBombs()) {
                    String word = CoolKey.getDictionary().randomWord(gameLevel);
                    if (gameBombs.containsKey(word)) continue;
                    int wordWidth = gc.stringExtent(word).x;
                    int x = (wordWidth - 16) / 2 + random.nextInt(WIDTH - wordWidth - 8);
                    int y = -48 - random.nextInt(64);
                    Bomb b = new Bomb(word, x, y, gameHowFast());
                    gameBombs.put(b.getWord(), b);
                }
                gc.dispose();
                imageTmp.dispose();
            }
        }, "gameCreateBombs");
        gameCreateBombsThread.start();
    }

    /**
	 * Odgrywa dźwięk eksplozji bomby.
	 */
    private synchronized void gameSoundExplosion() {
        Thread gameSoundExplosionThread = new Thread(new Runnable() {

            public void run() {
                if (CoolKey.isSoundAvailable()) CoolKey.getSoundBank().EXPLOSION.play();
            }
        }, "gameSoundExplosion");
        gameSoundExplosionThread.start();
    }

    /**
	 * Odgrywa dźwięk zniszcenia bomby.
	 */
    private synchronized void gameSoundExplosion2() {
        Thread gameSoundExplosionThread = new Thread(new Runnable() {

            public void run() {
                if (CoolKey.isSoundAvailable()) CoolKey.getSoundBank().EXPLOSION2.play();
            }
        }, "gameSoundExplosion2");
        gameSoundExplosionThread.start();
    }

    /**
	 * Dodaje wynik gry do listy najlepszych wyników.
	 * @param score liczba zdobytych punktów
	 * @param time czas trwania gry
	 * @param level poziom na którym gracz skończył grę
	 */
    private synchronized void top10ScoreAdd(final int score, final long time, final int level) {
        Thread top10ScoreAddThread = new Thread(new Runnable() {

            public void run() {
                List<Score> highscore = CoolKey.getUser().getHighscore();
                for (int i = 0; i < TOP10_RESULTS; i++) {
                    Score scoreObj = null;
                    try {
                        scoreObj = highscore.get(i);
                    } catch (IndexOutOfBoundsException e) {
                        highscore.add(i, new Score(score, time, level));
                        CoolKey.getUser().setHighscore(highscore);
                        CoolKey.persistState();
                        return;
                    }
                    if (scoreObj.getScore() < gameScore) {
                        highscore.set(i, new Score(score, time, level));
                        Score scoreObjTmp = null;
                        for (int j = i + 1; j < TOP10_RESULTS; j++) {
                            try {
                                scoreObjTmp = highscore.get(j);
                            } catch (IndexOutOfBoundsException e) {
                                highscore.add(j, scoreObj);
                                break;
                            }
                            highscore.set(j, scoreObj);
                            scoreObj = scoreObjTmp;
                        }
                        CoolKey.getUser().setHighscore(highscore);
                        CoolKey.persistState();
                        return;
                    }
                }
            }
        }, "top10ScoreAdd");
        top10ScoreAddThread.start();
    }

    /**
	 * Przelicza kolejną klatkę animacji.
	 */
    private synchronized void action() {
        switch(this.state) {
            case STATE_GAME:
                if (this.gameLevel < GAME_LEVEL_MAX && this.gameScore > this.gameHowScore()) this.gameLevel++;
                List<Bomb> bombs = new ArrayList<Bomb>(this.gameGetBombs());
                for (int i = 0; i < bombs.size(); i++) {
                    Bomb bomb = bombs.get(i);
                    bomb.addY(bomb.getSpeed() / this.fps);
                    if (bomb.getY() + 48 > GAME_GROUND) {
                        this.gameLife--;
                        this.gameSoundExplosion();
                        this.gameBombsExplosion.add(bomb);
                        this.gameBombs.remove(bomb.getWord());
                    }
                }
                for (int i = 0; i < this.gameBombsExplosion.size(); i++) {
                    Bomb bomb = this.gameBombsExplosion.get(i);
                    bomb.addExplosionStep(GAME_EXPLOSION_SPEED / this.fps);
                    if (bomb.getExplosionStep() > 9) this.gameBombsExplosion.remove(i);
                }
                this.gameCreateBombs();
                if (this.gameLife < 1) {
                    this.top10ScoreAdd(this.gameScore, this.gameTime, this.gameLevel);
                    this.newGame = true;
                    this.state = STATE_RESULT;
                }
                break;
        }
    }

    public synchronized void run() {
        long timeStart = System.currentTimeMillis();
        int time = (int) (timeStart - this.timeLast);
        if (time < 1000) {
            this.countFrame++;
        } else {
            this.fps = this.countFrame * 1000.0 / time;
            this.countFrame = 1;
            this.timeLast += time;
        }
        this.action();
        if (!this.container.isDisposed()) this.container.redraw(); else this.stop();
        time = INTERVAL - (int) (System.currentTimeMillis() - timeStart);
        this.display.timerExec((time < 0 ? 0 : time), this);
    }

    /**
	 * Uruchamia mechanizm animacji gry.
	 */
    public synchronized void start() {
        this.gameTimer.schedule(new TimerTask() {

            private long timeBefor = System.currentTimeMillis();

            public void run() {
                long timeCurrent = System.currentTimeMillis();
                if (state == STATE_GAME) {
                    gameTime += timeCurrent - timeBefor;
                }
                timeBefor = timeCurrent;
            }
        }, INTERVAL, INTERVAL);
        this.countFrame = 1;
        this.timeLast = System.currentTimeMillis();
        this.display.timerExec(0, this);
    }

    /**
	 * Zatrzymuję mechanizm animacji gry.
	 */
    public synchronized void stop() {
        this.gameTimer.cancel();
        if (!this.display.isDisposed()) this.display.timerExec(-1, this);
    }
}
