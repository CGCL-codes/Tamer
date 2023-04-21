package physic;

import java.util.Vector;

public class PhShip extends PhObject {

    protected static double MaxEnergy = 10;

    protected static double MaxHealth = 10;

    protected static double R = 10;

    protected static int Lives = 10;

    protected static double Wmax = 20;

    protected static double Wmin = 10;

    protected static double Ws = 10;

    protected double energy, max_energy;

    protected int lives;

    protected Vector<physic.Weapon> weapons;

    protected String cevent;

    protected double power, shield, regeneration, solar_battery, engine;

    protected double w_min, w_max, angle, cur_w, ws;

    protected double timels;

    PhShip(String name, double x, double y) {
        super();
        this.name = "s_" + name;
        this.max_energy = PhShip.MaxEnergy;
        this.max_health = PhShip.MaxHealth;
        this.energy = this.max_energy;
        this.health = this.max_health;
        this.lives = Lives;
        this.vx = 0;
        this.vy = 0;
        this.x = x;
        this.y = y;
        this.r = R;
        this.timels = -1000000000;
        weapons = new Vector();
        cevent = "";
        this.w_max = Wmax;
        this.w_min = Wmin;
        this.ws = Ws;
    }

    public void SetEnergy(double energy) {
        this.energy = energy;
    }

    public void SetMaxEnergy(double max_energy) {
        this.max_energy = max_energy;
    }

    public void SetLives(int lives) {
        this.lives = lives;
    }

    public void SetEvent(String event) {
        this.cevent = event;
    }

    public void SetShield(double shield) {
        this.shield = shield;
    }

    public void SetPower(double power) {
        this.power = power;
    }

    public void SetRegeration(double regeneration) {
        this.regeneration = regeneration;
    }

    public void SetSolarBattery(double sb) {
        this.solar_battery = sb;
    }

    public void SetEngine(double engine) {
        this.engine = engine;
    }

    public void SetTimeLS(double time) {
        this.timels = time;
    }

    public void SetW(double wmin, double wmax, double ws) {
        this.w_max = wmax;
        this.w_min = wmin;
        this.ws = ws;
    }

    public void SetAngle(double angle) {
        this.angle = angle;
    }

    public double GetEnergy() {
        return this.energy;
    }

    public double GetMaxEnergy() {
        return this.max_energy;
    }

    public int GetLives() {
        return this.lives;
    }

    public Weapon GetWeapon(int index) {
        return this.weapons.get(index);
    }

    public Vector<physic.Weapon> GetVectorWeapon() {
        return this.weapons;
    }

    public String GetEvent() {
        return this.cevent;
    }

    public double GetShield() {
        return this.shield;
    }

    public double GetPower() {
        return this.power;
    }

    public double GetRegeration() {
        return this.regeneration;
    }

    public double GetSolarBattery() {
        return this.solar_battery;
    }

    public double GetEngine() {
        return this.engine;
    }

    public double GetTimeLS() {
        return this.timels;
    }

    public double GetWmin() {
        return this.w_min;
    }

    public double GetWmax() {
        return this.w_max;
    }

    public double GetWspeed() {
        return this.ws;
    }

    public double GetAngle() {
        return this.angle;
    }

    public void AddWeapon(Weapon w) {
        this.weapons.add(w.clone());
    }

    public void mystep(Vector<physic.PhObject> wobjects, Vector<java.lang.Integer> gravindex) {
        this.cevent = "";
        this.health += PhShip.dt * this.regeneration;
        for (int i = 0; i < gravindex.size(); i++) {
            PhStar star = (PhStar) wobjects.get(gravindex.get(i));
            this.GetLight(star);
        }
        if (this.health > this.max_health) {
            this.health = this.max_health;
        }
    }

    public void Rotate(int side) {
        if ((Math.abs(this.cur_w) < eps) && (side != 0)) {
            this.cur_w = side * this.w_min;
        } else {
            this.cur_w += side * this.ws * dt;
        }
        if (Math.abs(this.cur_w) > this.w_max) {
            this.cur_w = side * this.w_max;
        }
        if (side == 0) {
            this.cur_w = 0;
        }
        this.angle += this.cur_w * PhShip.dt;
    }

    public void Accel() {
        if (this.energy > this.engine * dt) {
            this.vx += dt * Math.cos(this.angle) * this.power / this.m;
            this.vy += dt * Math.sin(this.angle) * this.power / this.m;
            this.energy -= this.engine * dt;
        }
    }

    public void GetLight(PhStar star) {
        this.energy += dt * this.solar_battery * PhShip.K_light * (star.shine * (this.r * this.r) / (this.dist(star) * this.dist(star)));
        if (this.energy > this.max_energy) {
            this.energy = this.max_energy;
        }
    }
}
