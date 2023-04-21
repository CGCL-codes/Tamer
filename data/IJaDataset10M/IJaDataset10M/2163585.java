package br.com.thelastsurvivor.model.game;

import java.io.Serializable;
import br.com.thelastsurvivor.util.Vector2D;

public class Asteroid implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer game;

    private Vector2D position;

    private Double angle;

    private Integer life;

    private Integer route;

    private Integer type;

    public Asteroid(Vector2D position, Double angle, Integer life, Integer route, Integer type) {
        this.position = position;
        this.angle = angle;
        this.life = life;
        this.route = route;
        this.type = type;
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

    public Integer getRoute() {
        return route;
    }

    public void setRoute(Integer route) {
        this.route = route;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
}
