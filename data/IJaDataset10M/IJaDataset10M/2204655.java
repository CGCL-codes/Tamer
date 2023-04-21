package br.com.thelastsurvivor.model.game;

import java.io.Serializable;
import java.util.List;
import br.com.thelastsurvivor.util.Vector2D;

public class Spacecraft implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer game;

    private Vector2D position;

    private Double angle;

    private Integer life;

    private Integer points;

    private List<Shoot> shoots;

    public Spacecraft(Vector2D position, Double angle, Integer life, Integer points, List<Shoot> shoots) {
        this.position = position;
        this.angle = angle;
        this.life = life;
        this.points = points;
        this.shoots = shoots;
    }

    public Spacecraft(Integer id, Vector2D position, Double angle, Integer life, Integer points) {
        this.id = id;
        this.position = position;
        this.angle = angle;
        this.life = life;
        this.points = points;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGame() {
        return game;
    }

    public void setGame(Integer game) {
        this.game = game;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<Shoot> getShoots() {
        return shoots;
    }

    public void setShoots(List<Shoot> shoots) {
        this.shoots = shoots;
    }
}
