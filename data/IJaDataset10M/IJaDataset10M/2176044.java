package physic;

import java.util.Vector;

public class PhWorld {

    public static final double pi = 3.1415;

    protected static double W = 10;

    protected double Max_x = 200;

    protected double Max_y = 200;

    protected Vector<physic.PhObject> wobjects;

    protected Vector<java.lang.Integer> gravindex;

    protected double time;

    protected Vector<java.lang.String> names;

    protected Vector<java.lang.Integer> index;

    protected int shnum;

    public PhWorld() {
        this.time = 0;
        this.wobjects = new Vector();
        this.gravindex = new Vector();
        this.index = new Vector();
        this.names = new Vector();
    }

    public void SetTime(double time) {
        this.time = time;
    }

    public void SetMaxX(double x) {
        this.Max_x = x;
    }

    public void SetMaxY(double y) {
        this.Max_y = y;
    }

    public void SetRes(double x, double y) {
        this.Max_x = x;
        this.Max_y = y;
    }

    public double GetTime() {
        return this.time;
    }

    public Vector<physic.PhObject> GetObjects() {
        return this.wobjects;
    }

    public double GetMaxX() {
        return this.Max_x;
    }

    public double GetMaxY() {
        return this.Max_y;
    }

    public void Load() {
    }

    public void Init() {
        for (int i = 0; i < this.names.size(); i++) {
            this.GenCoord(this.index.get(i));
        }
    }

    public void Fire(PhObject shooter, Weapon weapon) {
        double vx, vy, x, y;
        double px = 0, py = 0;
        vx = shooter.vx;
        vy = shooter.vy;
        x = shooter.x;
        y = shooter.y;
        double tvx, tvy, angle, tx, ty;
        if (shooter.getClass().getName().equals("physic.PhShip")) {
            double ls, energy;
            PhShip tmp;
            tmp = (PhShip) shooter;
            ls = tmp.GetTimeLS();
            energy = tmp.GetEnergy();
            if ((tmp.mytime - ls >= weapon.reloading) && (energy >= weapon.energy)) {
                for (int i = 0; i < weapon.GetVR().size(); i++) {
                    angle = weapon.GetAngle(i);
                    tvx = weapon.GetSpeed(i) * Math.cos(angle + tmp.angle) + vx;
                    tvy = weapon.GetSpeed(i) * Math.sin(angle + tmp.angle) + vy;
                    px += tvx * weapon.GetBullet(i).GetM();
                    py += tvy * weapon.GetBullet(i).GetM();
                    tx = x + weapon.GetR(i) * Math.cos(angle + tmp.angle);
                    ty = y + weapon.GetR(i) * Math.sin(angle + tmp.angle);
                    PhBullet temp = new PhBullet();
                    temp = weapon.GetBullet(i);
                    temp.SetAngle(angle + tmp.angle);
                    this.AddBullet(tx, ty, tvx, tvy, temp);
                }
                tmp.SetTimeLS(tmp.mytime);
                tmp.energy -= weapon.energy;
            }
        } else {
            PhBullet tmp;
            tmp = (PhBullet) shooter;
            for (int i = 0; i < weapon.GetVR().size(); i++) {
                angle = weapon.GetAngle(i);
                tvx = weapon.GetSpeed(i) * Math.cos(angle + tmp.angle) + vx;
                tvy = weapon.GetSpeed(i) * Math.sin(angle + tmp.angle) + vy;
                tx = x + weapon.GetR(i) * Math.cos(angle + tmp.angle);
                ty = y + weapon.GetR(i) * Math.sin(angle + tmp.angle);
                px += (tvx - vx) * weapon.GetBullet(i).GetM();
                py += (tvy - vy) * weapon.GetBullet(i).GetM();
                PhBullet temp = new PhBullet();
                temp = weapon.GetBullet(i);
                temp.SetAngle(angle + tmp.angle);
                this.AddBullet(tx, ty, tvx, tvy, temp);
            }
        }
        if (shooter.GetM() > 0) {
            shooter.SetVx(shooter.GetVx() - px / shooter.GetM());
            shooter.SetVy(shooter.GetVy() - py / shooter.GetM());
        }
    }

    public int SendCommand(String name, String event) {
        if (this.names.size() == 0) {
            return 0;
        }
        int i = 0;
        while (!(this.names.get(i).equals(name))) {
            i++;
            if (i == this.names.size()) {
                break;
            }
        }
        if (i == this.names.size()) {
            return 0;
        }
        i = this.index.get(i);
        if (this.wobjects.get(i).getClass().getName().equals("physic.PhShip")) {
            PhShip temp;
            temp = (PhShip) this.wobjects.get(i);
            temp.SetEvent(event);
        }
        return 1;
    }

    public void ShipEvents(PhShip ship) {
        String s, ts;
        int rotation = 0;
        s = ship.GetEvent();
        int i = 0;
        while (i < s.length()) {
            ts = s.substring(i, i + 1);
            if (ts.equals("A")) {
                ship.Accel();
            }
            if (ts.equals("R")) {
                rotation++;
            }
            if (ts.equals("L")) {
                rotation--;
            }
            if (ts.equals("F")) {
                i++;
                ts = s.substring(i, i + 1);
                int j = new Integer(ts).intValue();
                this.Fire(ship, ship.GetWeapon(j));
            }
            if (ts.equals("D")) {
                ship.SetHealth(-1);
            }
            i++;
        }
        if (rotation > 0) {
            rotation = 1;
        }
        if (rotation < 0) {
            rotation = -1;
        }
        ship.Rotate(rotation);
    }

    public void BulletEvent(PhBullet bullet) {
        String s, ts;
        int rotation = 0;
        s = bullet.GetCEvent();
        int i = 0;
        while (i < s.length()) {
            ts = s.substring(i, i + 1);
            if (ts.equals("A")) {
                bullet.Accel();
            }
            if (ts.equals("F")) {
                i++;
                ts = s.substring(i, i + 1);
                int j = java.lang.Integer.parseInt(ts);
                this.Fire(bullet, bullet.GetWeapon(j));
            }
            if (ts.equals("D")) {
                bullet.SetHealth(-1);
            }
            i++;
        }
    }

    public void AddStar(double x, double y) {
        PhObject temp;
        temp = new PhStar(x, y);
        this.wobjects.add(temp);
        this.gravindex.add(this.wobjects.size() - 1);
    }

    public void AddStar(double x, double y, double vx, double vy) {
        PhObject temp;
        temp = new PhStar(x, y, vx, vy);
        this.wobjects.add(temp);
        this.gravindex.add(this.wobjects.size() - 1);
    }

    public void AddPlanet(double x, double y) {
        PhObject temp;
        temp = new PhPlanet(x, y);
        this.wobjects.add(temp);
    }

    public void AddPlanet(double x, double y, double vx, double vy) {
        PhObject temp;
        temp = new PhPlanet(x, y, vx, vy);
        this.wobjects.add(temp);
    }

    public void AddMeteor(double x, double y) {
        PhObject temp;
        temp = new PhMeteor(x, y);
        this.wobjects.add(temp);
    }

    public void AddMeteor(double x, double y, double vx, double vy) {
        PhObject temp;
        temp = new PhMeteor(x, y, vx, vy);
        this.wobjects.add(temp);
    }

    public void GenCoord(int index) {
        double t, ang;
        t = this.time;
        int i = 0;
        while (i < this.names.size()) {
            if (this.index.get(i) == index) {
                break;
            }
            i++;
        }
        ang = this.W * t + 2 * pi * (1.0 * i) / (1.0 * shnum);
        this.wobjects.get(index).SetX(200 + 0.75 * this.Max_x * Math.cos(ang) + this.Max_x / 2);
        this.wobjects.get(index).SetY(200 + 0.75 * this.Max_y * Math.sin(ang) + this.Max_y / 2);
        this.wobjects.get(index).SetV(0, 0);
    }

    public void AddShip(String name) {
        PhShip ship;
        int index;
        index = this.wobjects.size();
        ship = new PhShip(name, 0.0, 0.0);
        this.names.add(name);
        this.index.add(index);
        this.wobjects.add(ship);
        shnum = this.index.size();
    }

    public void AddBullet(double x, double y) {
        PhBullet temp = new PhBullet();
        temp.SetX(x);
        temp.SetY(y);
        this.wobjects.add(temp);
    }

    public void AddBullet(double x, double y, double vx, double vy) {
        PhBullet temp = new PhBullet();
        temp.SetX(x);
        temp.SetY(y);
        temp.SetVx(vx);
        temp.SetVy(vy);
        temp.SetAngle(Math.atan2(vy, vx));
        this.wobjects.add(temp);
    }

    public void AddBullet(double x, double y, double vx, double vy, PhBullet primitive) {
        PhBullet temp = new PhBullet();
        temp = primitive.clone();
        temp.SetX(x);
        temp.SetY(y);
        temp.SetVx(vx);
        temp.SetVy(vy);
        this.wobjects.add(temp);
    }

    public void AddGuardian(double x, double y) {
        PhGuardian temp = new PhGuardian(x, y);
    }

    public void collision() {
        Vector<java.lang.Integer> ind_pl = new Vector();
        Vector<java.lang.Integer> ind_o = new Vector();
        for (int i = 0; i < this.wobjects.size(); i++) {
            if (this.wobjects.get(i).GetName().equals("Star")) {
                continue;
            }
            if (this.wobjects.get(i).GetName().equals("Planet")) {
                ind_pl.add(i);
                continue;
            }
            if (this.wobjects.get(i).GetName().equals("Guardian")) {
                continue;
            }
            ind_o.add(i);
        }
        int ii, jj;
        for (int i = 0; i < this.gravindex.size(); i++) {
            for (int j = 0; j < this.gravindex.size(); j++) {
                if (i < j) {
                    ii = this.gravindex.get(i);
                    jj = this.gravindex.get(j);
                    this.wobjects.get(ii).Collision(this.wobjects.get(jj));
                }
            }
            for (int j = 0; j < ind_pl.size(); j++) {
                ii = this.gravindex.get(i);
                jj = ind_pl.get(j);
                this.wobjects.get(ii).Collision(this.wobjects.get(jj));
            }
            for (int j = 0; j < ind_o.size(); j++) {
                ii = this.gravindex.get(i);
                jj = ind_o.get(j);
                this.wobjects.get(ii).Collision(this.wobjects.get(jj));
            }
        }
        for (int i = 0; i < ind_pl.size(); i++) {
            for (int j = 0; j < ind_pl.size(); j++) {
                if (i < j) {
                    ii = ind_pl.get(i);
                    jj = ind_pl.get(j);
                    this.wobjects.get(ii).Collision(this.wobjects.get(jj));
                }
            }
            for (int j = 0; j < ind_o.size(); j++) {
                ii = ind_pl.get(i);
                jj = ind_o.get(j);
                this.wobjects.get(ii).Collision(this.wobjects.get(jj));
            }
        }
        double maxr = -1;
        for (int i = 0; i < ind_o.size(); i++) {
            ii = ind_o.get(i);
            if (maxr < this.wobjects.get(ii).GetR()) {
                maxr = this.wobjects.get(ii).GetR();
            }
        }
        this.dichotomy(ind_o, 0, maxr);
    }

    public int dichotomy(Vector<java.lang.Integer> index, int axis, double r) {
        Vector<java.lang.Integer> right = new Vector();
        Vector<java.lang.Integer> left = new Vector();
        Vector<java.lang.Integer> nright = new Vector();
        Vector<java.lang.Integer> nleft = new Vector();
        int i, j, n;
        n = index.size();
        if (n < 2) {
            return n;
        }
        if (n == 2) {
            this.wobjects.get(index.get(0)).Collision(this.wobjects.get(index.get(1)));
            return 2;
        }
        double c = 0;
        c = this.middle(index, axis);
        for (i = 0; i < n; i++) {
            int temp = compare(this.wobjects.get(index.get(i)), axis, r, c);
            if (temp > 0) {
                right.add(index.get(i));
                if (temp == 2) {
                    nright.add(index.get(i));
                }
            } else {
                left.add(index.get(i));
                if (temp == -2) {
                    nleft.add(index.get(i));
                }
            }
        }
        int ii, jj;
        for (i = 0; i < nright.size(); i++) {
            for (j = 0; j < nleft.size(); j++) {
                ii = nright.get(i);
                jj = nleft.get(j);
                this.wobjects.get(ii).Collision(this.wobjects.get(jj));
            }
        }
        int tempaxis;
        tempaxis = (axis + 1) % 2;
        this.dichotomy(right, tempaxis, r);
        this.dichotomy(left, tempaxis, r);
        return n;
    }

    protected int compare(PhObject obj, int axis, double r, double c) {
        if (axis == 0) {
            if (obj.GetX() < c) {
                if (obj.GetX() < c - 2 * r) {
                    return -1;
                }
                return -2;
            } else {
                if (obj.GetX() > c + 2 * r) {
                    return 1;
                }
                return 2;
            }
        } else {
            if (obj.GetY() < c) {
                if (obj.GetY() < c - 2 * r) {
                    return -1;
                }
                return -2;
            } else {
                if (obj.GetY() > c + 2 * r) {
                    return 1;
                }
                return 2;
            }
        }
    }

    protected double middle(Vector<java.lang.Integer> index, double axis) {
        double mass[];
        int i, n;
        n = index.size();
        mass = new double[n + 1];
        if (axis == 0) {
            for (i = 0; i < n; i++) {
                mass[i] = this.wobjects.get(index.get(i)).GetX();
            }
        } else {
            for (i = 0; i < n; i++) {
                mass[i] = this.wobjects.get(index.get(i)).GetY();
            }
        }
        int l, r, k;
        double a, b;
        l = 0;
        r = n - 1;
        k = (n - 1) / 2;
        a = this.find(l, r, k, mass);
        k++;
        b = this.find(l, r, k, mass);
        return (a + b) / 2.0;
    }

    protected double find(int l, int r, int k, double mass[]) {
        int i, j;
        double m;
        double temp;
        i = l;
        j = r;
        m = mass[(i + j) / 2];
        while (i <= j) {
            while (mass[i] < m) {
                i++;
            }
            while (mass[j] > m) {
                j--;
            }
            if (i <= j) {
                temp = mass[i];
                mass[i] = mass[j];
                mass[j] = temp;
                i++;
                j--;
            }
        }
        if (i <= k) {
            if (i < r) {
                return find(i, r, k, mass);
            } else {
                return mass[i];
            }
        } else {
            if (l < i - 1) {
                return find(l, i - 1, k, mass);
            } else {
                return mass[i - 1];
            }
        }
    }

    public void ChgIndex(int index) {
        int i = 0;
        while (i < this.gravindex.size()) {
            int j = this.gravindex.get(i);
            if (j == index) {
                this.gravindex.remove(i);
                continue;
            }
            if (j > index) {
                this.gravindex.set(i, j - 1);
            }
            i++;
        }
        i = 0;
        while (i < this.index.size()) {
            int j = this.index.get(i);
            if (j == index) {
                this.index.remove(i);
                this.names.remove(i);
                continue;
            }
            if (j > index) {
                this.index.set(i, j - 1);
            }
            i++;
        }
    }

    public int step() {
        if ((this.wobjects.size() == 0)) {
            return 0;
        }
        int i, j;
        i = 0;
        for (i = 0; i < this.wobjects.size(); i++) {
            for (j = 0; j < this.gravindex.size(); j++) {
                if (i != this.gravindex.get(j)) {
                    this.wobjects.get(i).ForseTo(this.wobjects.get(this.gravindex.get(j)));
                }
            }
        }
        this.collision();
        this.wobjects.get(0).incTime();
        for (i = 0; i < this.wobjects.size(); i++) {
            this.wobjects.get(i).step();
            int otype = 0;
            if (this.wobjects.get(i).getClass().getName().equals("physic.PhShip")) {
                otype = 1;
            }
            if (this.wobjects.get(i).getClass().getName().equals("physic.PhBullet")) {
                otype = 2;
            }
            if (otype == 1) {
                PhShip temp;
                temp = (PhShip) this.wobjects.get(i);
                this.ShipEvents(temp);
                temp.mystep(wobjects, gravindex);
            }
            if (otype == 2) {
                PhBullet temp;
                temp = (PhBullet) this.wobjects.get(i);
                this.wobjects.get(i).mystep();
                this.BulletEvent(temp);
                this.wobjects.get(i).mystep();
                this.BulletEvent(temp);
            }
            if (otype == 0) {
                this.wobjects.get(i).mystep();
            }
        }
        i = 0;
        while (i < this.wobjects.size()) {
            if (this.wobjects.get(i).GetHealth() <= 0) {
                if (this.wobjects.get(i).getClass().getName().equals("physic.PhStar")) {
                    this.wobjects.remove(i);
                    int k = 0;
                    while (k < this.gravindex.size()) {
                        if (this.gravindex.get(k) == i) {
                            this.gravindex.remove(k);
                            break;
                        }
                        k++;
                    }
                    continue;
                }
                if (this.wobjects.get(i).getClass().getName().equals("physic.PhPlanet")) {
                    this.wobjects.remove(i);
                    this.ChgIndex(i);
                    continue;
                }
                if (this.wobjects.get(i).getClass().getName().equals("physic.PhMeteor")) {
                    this.wobjects.remove(i);
                    this.ChgIndex(i);
                    continue;
                }
                if (this.wobjects.get(i).getClass().getName().equals("physic.PhShip")) {
                    PhShip temp = (PhShip) this.wobjects.get(i);
                    if (temp.GetLives() == 1) {
                        temp.SetLives(0);
                        this.wobjects.remove(i);
                        this.ChgIndex(i);
                        continue;
                    } else {
                        temp.SetLives(temp.GetLives() - 1);
                        temp.SetHealth(temp.GetMaxHealth());
                        temp.SetEnergy(temp.GetMaxEnergy());
                        this.GenCoord(i);
                    }
                }
                if (this.wobjects.get(i).getClass().getName().equals("physic.PhBullet")) {
                    this.wobjects.remove(i);
                    this.ChgIndex(i);
                    continue;
                }
            }
            i++;
        }
        this.time = this.wobjects.get(0).GetTime();
        for (i = 0; i < this.wobjects.size(); i++) {
            wobjects.get(i).SetTime(this.time);
        }
        return 1;
    }

    public int macrostep(double t) {
        double ot = this.time;
        while (this.time - ot <= t) {
            if (this.step() == 0) {
                return 0;
            }
        }
        return 1;
    }
}
