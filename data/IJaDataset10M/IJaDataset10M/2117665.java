package gameobjects;

import gamecontrol.Box;

public class Element {

    private char id;

    private Box box;

    private int priority;

    public static final int HeroPriority = 1;

    public static final int FireFlyPriority = 2;

    public static final int ButterFlyPriority = 2;

    public static final int AmoebaPriority = 2;

    public static final int WeightPriority = 2;

    public static final int MagicWallPriority = 2;

    public static final int ExpandingWallPriority = 2;

    public static final int BeerPriority = 2;

    public static final int DirtyTerrainPriority = 3;

    public static final int CleanTerrainPriority = 3;

    public static final int ExitPriority = 3;

    public static final int WallPriority = 3;

    public static final char HeroId = 'H';

    public static final char FireFlyId = 'F';

    public static final char ButterFlyId = 'T';

    public static final char AmoebaId = 'A';

    public static final char WeightId = 'W';

    public static final char MagicWallId = 'M';

    public static final char ExpandingWallId = 'E';

    public static final char BeerId = 'B';

    public static final char DirtyTerrainId = 'D';

    public static final char CleanTerrainId = ' ';

    public static final char ExitId = 'O';

    public static final char WallId = '+';

    /**
         * First Contructor
         * @param id
         * @param box
         * @param priority
         */
    public Element(char id, Box box, int priority) {
        this.id = id;
        this.box = box;
        this.priority = priority;
    }

    /**
         * Second Constructor
         */
    public Element() {
        this.id = '-';
        this.box = null;
        this.priority = -1;
    }

    public Element destroy() {
        return null;
    }

    /**
         * 
         * @return the id
         */
    public char getId() {
        return id;
    }

    /**
         * 
         * @param id
         */
    public void setId(char id) {
        this.id = id;
    }

    /**
         * 
         * @return the box assigned
         */
    public Box getBox() {
        return this.box;
    }

    /**
         * 
         * @param box
         */
    public void setBox(Box box) {
        this.box = box;
    }

    /**
         * 
         * @return the priority to move the element
         */
    public int getPriority() {
        return this.priority;
    }

    /**
         * 
         * @param priority
         */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
