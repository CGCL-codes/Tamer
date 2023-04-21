package thimeeeee.Model;

import thimeeeee.Control.GameController;

public class Game {

    GameInfo userinfo;

    private LevelFactory levelfactory;

    private Level _level;

    private int numLevel = 1;

    GameController gameController;

    Player player1, player2;

    Diamond diamond;

    public Beast b0, b1, b2, b3, b4, b5, b6, b7;

    public Game(GameController gc) {
        Init(numLevel);
        gameController = gc;
    }

    public Game(int level, GameController gc) {
        numLevel = level;
        Init(level);
        gameController = gc;
    }

    public Level GetLevel() {
        return _level;
    }

    public Player GetPlayer(int i) {
        if (i == 2) {
            return player2;
        } else return player1;
    }

    public GameInfo GetGameInfo() {
        return userinfo;
    }

    public void Init(int level) {
        levelfactory = new LevelFactory(this);
        userinfo = new GameInfo(this);
        _level = levelfactory.GetLevel(level);
        if (player1 != null) player1.level = _level;
        if (player2 != null) player2.level = _level;
        userinfo.SetLevel(_level);
    }

    public void NextLevel() {
        gameController.Start(numLevel + 1);
    }

    public void KillPlayer() {
        userinfo.KillPlayer();
    }

    public void RestartLevel() {
        int lives = userinfo.GetLives();
        gameController.Start(numLevel, lives);
    }

    public void EndGame() {
        System.out.println("Game Over");
        gameController.GameEnd();
    }

    public void IncDiamond() {
        userinfo.IncDiamonds();
    }

    public void Idle(double aTimeStamp) {
        _level.Idle(aTimeStamp);
    }

    public char[][] GetStringMap() {
        char[][] map = new char[_level.maxHeight][_level.maxWidth];
        for (int i = 0; i < _level.maxHeight; i++) {
            for (int j = 0; j < _level.maxWidth; j++) {
                map[i][j] = ' ';
            }
        }
        Field rowF = _level.field;
        Field f = rowF;
        int i = 0;
        int j = 0;
        while (rowF != null) {
            j = 0;
            while (f != null) {
                if (f.HasChanged()) {
                    if (!f.IsEmpty()) {
                        ElementBase[] b = f.GetElements();
                        if (b[0] != null) {
                            if (b[0].getClass() == Soil.class) {
                                map[i][j] = '#';
                            }
                            if (b[0].getClass() == Granite.class) {
                                map[i][j] = 'g';
                            }
                            if (b[0].getClass() == Wall.class) {
                                map[i][j] = '*';
                            }
                            if (b[0].getClass() == Exit.class) {
                                map[i][j] = 'e';
                            }
                            if (b[0].getClass() == Rock.class) {
                                map[i][j] = 'r';
                            }
                            if (b[0].getClass() == Diamond.class) {
                                map[i][j] = 'd';
                            }
                            if (b[0].getClass() == Bomb.class) {
                                map[i][j] = 'b';
                            }
                            if (b[0].getClass() == Player.class) {
                                if (b[0].equals(player1)) {
                                    map[i][j] = 'k';
                                    if (b[1] != null && b[1].getClass() == Bomb.class) {
                                        map[i][j] = 'c';
                                    }
                                } else {
                                    map[i][j] = 'l';
                                    if (b[1] != null && b[1].getClass() == Bomb.class) {
                                        map[i][j] = 'n';
                                    }
                                }
                            }
                            if (b[0].getClass() == Beast.class) {
                                Beast sz = (Beast) b[0];
                                int[] szamok = { 0, 0, 0, 0, 0, 0, 0, 0 };
                                if (sz._isDiamond) {
                                    szamok[4]++;
                                    szamok[5]++;
                                    szamok[6]++;
                                    szamok[7]++;
                                }
                                if (sz._isReproducing) {
                                    szamok[2]++;
                                    szamok[3]++;
                                    szamok[6]++;
                                    szamok[7]++;
                                }
                                if (sz._isTracing) {
                                    szamok[1]++;
                                    szamok[3]++;
                                    szamok[5]++;
                                    szamok[7]++;
                                }
                                int beastSzam = 0;
                                for (int e = 1; e < 8; e++) {
                                    if (szamok[e] > szamok[beastSzam]) {
                                        beastSzam = e;
                                    }
                                }
                                map[i][j] = (char) (beastSzam + '0');
                            }
                        }
                    } else {
                        map[i][j] = '-';
                    }
                    f.UnSetChanged();
                }
                if (i % 2 == 0) {
                    if (f.GetNeighbor(Direction.downright) == null) {
                        if (f.GetNeighbor(Direction.upright) != null) {
                            Field f2 = f.GetNeighbor(Direction.upright);
                            if (f2.GetNeighbor(Direction.downright) != null) {
                                f = f2.GetNeighbor(Direction.downright);
                                j++;
                            } else f = null;
                        } else f = null;
                    } else {
                        f = f.GetNeighbor(Direction.downright);
                        i++;
                    }
                } else {
                    f = f.GetNeighbor(Direction.upright);
                    i--;
                    j++;
                }
            }
            rowF = rowF.GetNeighbor(Direction.down);
            f = rowF;
            if (i % 2 == 0) i += 2; else i++;
        }
        return map;
    }
}
