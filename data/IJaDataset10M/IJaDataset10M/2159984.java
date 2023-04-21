package server.game;

import both.Global;
import both.ObjectId;
import both.util.EnemyTypes;
import both.util.EnemyInformation;
import both.util.Vector2;

/**
 * Die Enemy Klasse ermöglicht das Spawnen eines Gegners zufällig ausserhalb des Spielfeldrandes.
 * @author Basil
 */
public class Enemy extends GameObject {

    /**
	 * Infos über Größe usw.
	 */
    EnemyInformation eInfo;

    /**
	 * Konstruktor fuer einen Gegner ohne Beschleunigung.
	 * @param position Position des Gegners
	 * @param velocity Geschwindigkeit des Gegners
	 */
    public Enemy(Vector2 position, Vector2 velocity, float rotation, String type, Vector2 size, String textureName, int life) {
        super(position, rotation, velocity, ObjectId.valueOf(type.toUpperCase()), size, textureName, life);
        this.eInfo = EnemyTypes.get(type);
    }

    /**
	 * Ein Gegner wird zufällig ausserhalb entlang des Spielfeldrandes erzeugt.
	 * @param type Gibt den Gegnertyp an
	 * @return Den erzeugten Gegner
	 */
    public static Enemy spawn(String type) {
        EnemyInformation eInfo = EnemyTypes.get(type);
        Vector2 size = new Vector2(eInfo.width, eInfo.height);
        String textureName = eInfo.texture;
        int life = eInfo.life;
        float speed = eInfo.speed;
        float posX = 0, posY = 0;
        float random = (float) (Math.random() * Global.LOGIC_FIELD_SIZE);
        int side = (int) (Math.random() * 4);
        if (side == 0) {
            posX = random;
            posY = -size.getY();
        } else if (side == 1) {
            posX = Global.LOGIC_FIELD_SIZE + size.getX();
            posY = random;
        } else if (side == 2) {
            posX = random;
            posY = Global.LOGIC_FIELD_SIZE + size.getY();
        } else if (side == 3) {
            posX = -size.getX();
            posY = random;
        }
        Vector2 position = new Vector2(posX, posY);
        Vector2 velocity = new Vector2(Logic.getCenter().substract(position));
        velocity.normalize_inplace();
        float rotation = (float) Math.toDegrees(Math.atan(velocity.getY() / velocity.getX())) + 90f;
        if (velocity.getX() > 0) rotation += 180;
        velocity.multiplySkalar_inplace(speed);
        return new Enemy(position, velocity, rotation, type, size, textureName, life);
    }

    /**
	 * Bewegt den Enemy einen Schritt weiter
	 */
    @Override
    public void move(float time) {
        update(time);
        step(time);
        setHead();
    }

    /**
	 * Gegner in die richtige Position drehen
	 */
    protected void setHead() {
        this.rotation = (float) Math.toDegrees(Math.atan(velocity.getY() / velocity.getX())) + 90f;
        if (velocity.getX() > 0) this.rotation += 180;
    }

    /**
	 * Gibt die ObjectId des Gegners zurück
	 */
    public ObjectId getId() {
        return this.id;
    }

    /**
	 * Gibt das EnemyInformation objekt dieses Gegners zurück
	 * @return
	 */
    public EnemyInformation getEnemyInfo() {
        return this.eInfo;
    }
}
