package org.jazzteam.ModelHuman;

class Heart {

    private String endocardium;

    private String epicardium;

    private String myocardium;

    private int stateHealth = 100;

    public void setStateHealth(int stateHealth) {
        if (this.stateHealth + stateHealth <= 100) {
            this.stateHealth += stateHealth;
        } else this.stateHealth = 100;
    }

    public int getStateHealth() {
        return this.stateHealth;
    }
}
