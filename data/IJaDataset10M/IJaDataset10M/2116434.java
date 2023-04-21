package content;

public class PUpFactoryImplementation implements PowerUpFactory {

    @Override
    public PowerUp makePowerUp(int x, int y) {
        double firstProb = Math.random();
        double secondProb = Math.random();
        if (firstProb < .55) {
            if (secondProb < 0.599) return new WeaponPowerUp(x, y); else if (secondProb >= 0.599 && secondProb < 0.998) return new ShieldPowerUp(x, y); else return new freeManPUp(x, y);
        } else {
            if (secondProb < 0.499) return new BombPowerUp(x, y); else if (secondProb >= 0.499 && secondProb < 0.998) return new ShipRepairPUp(x, y); else return new freeManPUp(x, y);
        }
    }
}
