package com.teamamerica.games.unicodewars.object.towers;

import java.util.Random;
import com.teamamerica.games.unicodewars.system.BB;
import com.teamamerica.games.unicodewars.utils.Location;
import com.teamamerica.games.unicodewars.utils.Team;

public class DiceOne extends TowerBase {

    private static final int MAX_ATTACK = 20;

    private static final int MAX_SPEED = 20;

    public static int price = 5;

    public DiceOne(Location loc, Team team, String imgLoc) {
        super(TowerBase.Type.diceOne, 0, price, 0, 0, team, loc, imgLoc);
        Random gen = BB.inst().getRandom();
        this.attack = gen.nextInt(5) + 1;
        this.radius = gen.nextInt(5 - (this.attack - 1)) / 2 + 1;
        this.speed = 8 - (this.attack + this.radius);
        if (this.speed < 1) {
            this.speed = 1;
        }
        this.registerNewSpaces();
    }

    @Override
    public boolean canUpgrade() {
        return this.level < 6;
    }

    @Override
    public void doUpgrade() {
        if (_team == Team.Player1) BB.inst().getUsersPlayer().purchase(this.getUpgradePrice());
        this.level++;
        int totalAtt = 0;
        int totalSpd = 0;
        Random gen = BB.inst().getRandom();
        if (level == 2) {
            totalAtt = 3;
            totalSpd = 2;
            this.getVisualComponent().updateImage("data/images/towers/Dice-2.png");
        } else if (level == 3) {
            totalAtt = 3;
            totalSpd = 2;
            this.radius++;
            this.getVisualComponent().updateImage("data/images/towers/Dice-3.png");
        } else if (level == 4) {
            totalAtt = 4;
            totalSpd = 3;
            this.getVisualComponent().updateImage("data/images/towers/Dice-4.png");
        } else if (level == 5) {
            totalAtt = 4;
            totalSpd = 3;
            this.getVisualComponent().updateImage("data/images/towers/Dice-5.png");
        } else if (level == 6) {
            totalAtt = 5;
            totalAtt = 4;
            this.getVisualComponent().updateImage("data/images/towers/Dice-6.png");
        }
        int attkAdd = (this.attack <= this.MAX_ATTACK) ? gen.nextInt(totalAtt) : 0;
        this.attack += attkAdd;
        int speedAdd = (this.speed <= MAX_SPEED) ? gen.nextInt(totalAtt - (attkAdd - 1)) + 1 : 0;
        this.speed += speedAdd;
        if (level < 6 && this.attack >= MAX_ATTACK && this.speed >= MAX_SPEED) {
            level = 6;
            this.getVisualComponent().updateImage("data/images/towers/Dice-6.png");
        }
        super.doUpgrade();
    }

    @Override
    public String getInfoString() {
        return "Dice Tower Level " + this.level;
    }

    @Override
    public String getStatusString() {
        return null;
    }

    public int getUpgradePrice() {
        if (level == 1) {
            return 10;
        } else if (level == 2) {
            return 20;
        } else if (level == 3) {
            return 40;
        } else if (level == 4) {
            return 80;
        } else if (level == 5) {
            return 160;
        }
        return 0;
    }
}
