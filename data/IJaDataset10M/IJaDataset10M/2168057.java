package com.myapp.game.foxAndBunny.model;

public class Fox extends Animal {

    private static final int SEXUALLY_MATURE_AGE = 10;

    private static final int MAX_AGE = 150;

    private static final double BIRTH_PROPABILITY = 0.12;

    private static final int BIRTH_COUNT = 3;

    private static AnimalParameters parameters = new AnimalParameters(MAX_AGE, SEXUALLY_MATURE_AGE, BIRTH_PROPABILITY, BIRTH_COUNT);

    public static final int BUNNY_CALORIES = 4;

    private int caloryLevel;

    public Fox() {
        this(false);
    }

    public Fox(boolean randomAge) {
        super(randomAge);
        if (randomAge) {
            caloryLevel = RANDOM.nextInt(BUNNY_CALORIES);
        } else {
            caloryLevel = BUNNY_CALORIES;
        }
    }

    @Override
    protected void actImpl() {
        becomeHungry();
        if (!isAlive()) return;
        hunt();
        if (!isAlive()) return;
        reproduce();
    }

    @Override
    protected Animal createChild() {
        return new Fox();
    }

    private void becomeHungry() {
        caloryLevel--;
        if (caloryLevel <= 0) die();
    }

    private void hunt() {
        Field f = getField();
        Position bunnyPos = f.getRandomFullNeighbour(Bunny.class);
        if (bunnyPos == null) {
            super.walkToNextField();
            return;
        }
        Bunny gotcha = (Bunny) getWorld().getPopulation().getActor(bunnyPos);
        gotcha.die();
        caloryLevel = BUNNY_CALORIES;
        super.walkToField(bunnyPos);
    }

    @Override
    protected AnimalParameters params() {
        return parameters;
    }
}
