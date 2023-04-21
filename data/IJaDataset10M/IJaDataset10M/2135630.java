package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import com.googlecode.jumpnevolve.game.GroundTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Normaler Boden, zum Gestalten der Landschaft
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: keine
 * 
 * Aggressivitäten: keine
 * 
 * Immunitäten: keine
 * 
 * Aktivierung: keine
 * 
 * Deaktivierung: keine
 * 
 * Besonderheiten: beliebige Größe anhand eines Vektors
 * 
 * @author Erik Wagner
 * 
 */
public class Ground extends GroundTemplate {

    private static final long serialVersionUID = 7842858995624719370L;

    public Ground(World world, Vector position, Vector dimension) {
        super(world, ShapeFactory.createRectangle(position, dimension));
    }

    public Ground(World world, Vector position, String arguments) {
        this(world, position, Vector.parseVector(arguments));
    }

    @Override
    protected void specialSettingsPerRound(Input input) {
    }

    public void draw(Graphics g) {
        GraphicUtils.texture(g, getShape(), ResourceManager.getInstance().getImage("textures/stone.png"), false);
    }
}
