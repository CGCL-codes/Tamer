package com.seavenois.alife;

public class Box {

    private double width, heigth, pointX, pointY;

    private double drawX, drawY, drawW, drawH;

    private boolean buildable = true, riverbank = false, built = false;

    private byte building, level, color;

    private int capacity, consumption, workers, input, energy, train, tickets, age, beds;

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    private double vibes, emiting, eficacy;

    public double getVibes() {
        return vibes;
    }

    public void setVibes(double vibes) {
        this.vibes = vibes;
        if (vibes < -1) vibes = -1;
        if (vibes > 1) vibes = 1;
    }

    public double getEmiting() {
        return emiting;
    }

    public void setEmiting(double emiting) {
        this.emiting = emiting;
    }

    public double getEficacy() {
        eficacy = 0.8 * vibes + 0.1 * emiting + 0.1 * (Math.random() * 2 - 1);
        if (eficacy < -1) eficacy = -1;
        if (eficacy > 1) eficacy = 1;
        return eficacy;
    }

    public void setEficacy(double eficacy) {
        this.eficacy = eficacy;
        if (eficacy < -1) eficacy = -1;
        if (eficacy > 1) eficacy = 1;
    }

    public int getAge() {
        return age;
    }

    public void grow() {
        this.age++;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = (byte) color;
    }

    public boolean isBuilt() {
        return built;
    }

    public void setBuilt(boolean s) {
        boolean now = false;
        if (built == false) now = true;
        built = s;
        if (s == true && now == true) level = 1;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public void setBuildable(boolean buildable) {
        this.buildable = buildable;
    }

    public boolean isRiverbank() {
        return riverbank;
    }

    public void setRiverbank(boolean riverbank) {
        this.riverbank = riverbank;
    }

    Box(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y, double p4x, double p4y) {
        pointX = p1x - 0.4272 * (p4x - p2x) / 2;
        width = 2 * (p1x - pointX);
        pointY = p1y + 0.4118 * (p3y - p1y) / 2;
        heigth = 2 * 0.5882 * (p3y - p1y) / 2;
        drawX = p2x;
        drawY = p1y - heigth * 3;
        drawW = p4x;
        drawH = p3y;
        destroy();
    }

    Box(double px, double py, double bw, double bh) {
        pointX = px;
        pointY = py;
        width = bw;
        heigth = bh;
        drawX = px - bw / 2;
        drawY = py - bh * 2.5;
        drawW = px + bw / 2;
        drawH = py + bh;
        destroy();
    }

    public void move(double px, double py, double bw, double bh) {
        pointX = px;
        pointY = py;
        width = bw;
        heigth = bh;
        drawX = px - bw / 2;
        drawY = py - bh * 2.5;
        drawW = px + bw / 2;
        drawH = py + bh;
    }

    public void destroy() {
        building = 0;
        capacity = 0;
        consumption = 0;
        workers = 0;
        energy = 0;
        train = 0;
        tickets = 0;
        level = 0;
        input = 0;
        age = 0;
        color = Building.COLOR_NONE;
        eficacy = 0;
        emiting = 0;
        beds = 0;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = (byte) building;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getTrain() {
        return train;
    }

    public void setTrain(int train) {
        this.train = train;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    boolean contains(double x, double y) {
        boolean yes = false;
        double rel;
        if (x < pointX && x > pointX - width / 2 && y > pointY && y < pointY + heigth / 2) {
            rel = (pointX - x) / (width / 2);
            if (y > pointY + (heigth / 2) * rel) yes = true;
        }
        if (x > pointX && x < pointX + width / 2 && y > pointY && y < pointY + heigth / 2) {
            rel = (x - pointX) / (width / 2);
            if (y > pointY + (heigth / 2) * rel) yes = true;
        }
        if (x < pointX && x > pointX - width / 2 && y > pointY + heigth / 2 && y < pointY + heigth) {
            rel = (pointX - x) / (width / 2);
            if (y < pointY + heigth - (heigth / 2) * rel) yes = true;
        }
        if (x > pointX && x < pointX + width / 2 && y > pointY + heigth / 2 && y < pointY + heigth) {
            rel = (x - pointX) / (width / 2);
            if (y < pointY + heigth - (heigth / 2) * rel) yes = true;
        }
        return yes;
    }

    public double getWidth() {
        return width;
    }

    public double getHeigth() {
        return heigth;
    }

    public double getPointX() {
        return pointX;
    }

    public double getPointY() {
        return pointY;
    }

    public double getDrawX() {
        return drawX;
    }

    public double getDrawY() {
        return drawY;
    }

    public double getDrawW() {
        return drawW;
    }

    public double getDrawH() {
        return drawH;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getInput() {
        return input;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = (byte) level;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
