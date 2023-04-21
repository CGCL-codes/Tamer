package kirby;

import com.googlecode.boringengine.Animator;
import com.googlecode.boringengine.Graphics;
import com.googlecode.boringengine.Input;
import com.googlecode.boringengine.Render;
import com.googlecode.boringengine.SoundSystem;
import com.googlecode.boringengine.Sprite;
import com.googlecode.boringengine.lua.LuaFunction;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Kirby extends GameObject {

    private State state = State.NOT_MOVING;

    private double gravity;

    private boolean flipped = false, isFull, didCollideX, didCollideY;

    private short jumpPower = 10;

    private short fallPower = 10;

    private short deathPower = 0;

    private short unSuckPower = 5;

    private short swallowPower = 10;

    private short spittingoutPower = 10;

    private short invinciblePower = 0;

    private int health, maxHealth;

    private ArrayList<Projectile> projectiles;

    private Sprite particleSprite, particle2Sprite;

    public Kirby() {
        this(10, 268);
    }

    public Kirby(int x, int y) {
        anim = new Animator("resources/gfx/kirby", Resources.l);
        particleSprite = Graphics.loadSpriteFromFile("resources/gfx/particle.sprite");
        particle2Sprite = Graphics.loadSpriteFromFile("resources/gfx/particle2.sprite");
        this.x = x;
        this.y = y;
        w = h = 32;
        health = maxHealth = 999;
    }

    public void update0() {
        HUD.setDebugString(state.name().replace("_", " ") + " " + invinciblePower);
        if (invinciblePower > 0) {
            invinciblePower--;
            if (invinciblePower == 0) {
                SoundSystem.endTempMusic();
            }
        }
        if (isDead()) return;
        if (health < 1 && !isDying() && !isDead()) {
            die();
        }
        if (isDying()) {
            if (state == State.STARTED_DYING) {
                if (deathPower > 0) {
                    deathPower--;
                    return;
                }
                Camera.follow((GameObject) null);
                anim.changeAnimation("die");
                for (int i = 0; i < 360; i += 5) createDeathParticle(i);
            }
            state = State.DYING;
            y += gravity;
            gravity += .25;
            if (gravity > 0 && y > Camera.getBelowCamera()) state = State.DEAD;
            return;
        }
        if (state == State.SWALLOWING) {
            swallowPower--;
            if (swallowPower == 0) {
                isFull = false;
                anim.changeAnimation("stand");
                state = State.NOT_MOVING;
            }
            return;
        }
        if (state == State.SPITTING_OUT) {
            spittingoutPower--;
            if (spittingoutPower == 0) {
                isFull = false;
                anim.changeAnimation("stand");
                state = State.NOT_MOVING;
            }
            return;
        }
        if (state == State.STOPPED_SUCKING2) {
            unSuckPower--;
            if (unSuckPower == 0) {
                isFull = true;
                anim.changeAnimation("fullstand");
                state = State.NOT_MOVING;
            }
            return;
        }
        if (Input.isPressed(KeyEvent.VK_Z, 0)) {
            if (!isFlying() && !isSucking() && !isFull) {
                state = State.STARTED_SUCKING;
                anim.changeAnimation("suck");
                projectiles.add(new Projectile.Sucking(flipped, x + (flipped ? -32 : 16), y - 8));
            } else if (isFull) {
                state = State.SPITTING_OUT;
                spittingoutPower = 10;
                projectiles.add(new Projectile.Star(flipped, x + (flipped ? -25 : 25), y - 4));
                anim.changeAnimation("spitout");
                return;
            }
        } else if (state == State.STARTED_SUCKING) {
            state = State.STOPPED_SUCKING;
            unSuckPower = 5;
            anim.changeAnimation("endsuck");
            for (Projectile projectile : projectiles) if (projectile instanceof Projectile.Sucking) projectile.die();
            return;
        }
        if (state == State.STOPPED_SUCKING) {
            unSuckPower--;
            if (unSuckPower > 0) return;
            state = State.NOT_MOVING;
            anim.changeAnimation("stand");
            return;
        }
        if (isSucking()) {
            createSuckingParticle();
            for (int i = 0; i < gravity; i++) {
                int canMove = CollisionDetection.canMoveToY(x, y, w, h, 1);
                if (canMove == CollisionDetection.CANT_MOVE) {
                    if (gravity != 0.25) {
                        createCollisParticle();
                    }
                    gravity = 0;
                    jumpPower = 10;
                    fallPower = 10;
                    break;
                }
                y += 1;
                for (Projectile projectile : projectiles) if (projectile instanceof Projectile.Sucking) ((Projectile.Sucking) projectile).move(x + (flipped ? -32 : 16), y - 8);
            }
            gravity += .25;
            boolean isSucking = false;
            for (Projectile projectile : projectiles) if (projectile instanceof Projectile.Sucking) if (((Projectile.Sucking) projectile).stillSucking()) {
                state = State.SUCKING;
                isSucking = true;
            }
            if (state == State.STARTED_SUCKING) return;
            if (!isSucking) {
                anim.changeAnimation("endfullsuck");
                state = State.STOPPED_SUCKING2;
                unSuckPower = 10;
                for (Projectile projectile : projectiles) if (projectile instanceof Projectile.Sucking) projectile.die();
                return;
            }
            return;
        }
        int dx = 0, dy = 0;
        if (Input.isPressed(KeyEvent.VK_LEFT, -1)) {
            dx -= 2;
            flipped = true;
        }
        if (Input.isPressed(KeyEvent.VK_RIGHT, -1)) {
            dx += 2;
            flipped = false;
        }
        if (Input.isPressed(KeyEvent.VK_X, -1) && jumpPower > 0 && !isFlying()) {
            gravity = -5;
            jumpPower--;
            state = State.STARTED_JUMPING;
        }
        if (!Input.isPressed(KeyEvent.VK_X, -1) && isJumping()) jumpPower = 0;
        if (!isFull && (Input.isPressed(KeyEvent.VK_UP, -1) || (Input.isPressed(KeyEvent.VK_X, -1) && isFlying()))) {
            dy -= 5;
            gravity = 1;
            if (state != State.FLYING) state = State.STARTED_FLYING;
            jumpPower = 0;
        }
        if (Input.isPressed(KeyEvent.VK_DOWN, -1) && isFull) {
            state = State.SWALLOWING;
            anim.changeAnimation("swallow");
            swallowPower = 10;
            return;
        }
        if (Input.isPressed(KeyEvent.VK_Z, -1) && isFlying()) {
            state = State.STOPPED_FLYING;
            projectiles.add(new Projectile.Air(flipped, x + (flipped ? -25 : 25), y - 8));
        }
        dy += gravity;
        int dxabs = Math.abs(dx), dyabs = Math.abs(dy);
        for (int i = 0; i < dxabs; i++) {
            int canMove = CollisionDetection.canMoveToX(x, y, w, h, dxabs / dx);
            if (canMove == CollisionDetection.CANT_MOVE) {
                if (!didCollideX) createCollisParticle();
                didCollideX = true;
                break;
            }
            didCollideX = false;
            x += dxabs / dx;
        }
        for (int i = 0; i < dyabs; i++) {
            int canMove = CollisionDetection.canMoveToY(x, y, w, h, dyabs / dy);
            if (canMove == CollisionDetection.CANT_MOVE) {
                if (!didCollideY) {
                    didCollideY = true;
                    createCollisParticle();
                }
                if (dyabs / dy == 1) {
                    gravity = 0;
                    if (isJumping() && state != State.STOPPED_FLYING) {
                        state = State.STOPPED_JUMPING;
                    }
                    if (isFlying()) {
                        fallPower = 10;
                        projectiles.add(new Projectile.Air(flipped, x + (flipped ? -25 : 25), y - 8));
                        state = State.STOPPED_FLYING;
                    }
                    if (fallPower == 0) state = dx != 0 ? State.STARTED_MOVING : State.STOPPED_MOVING;
                    jumpPower = 10;
                } else {
                    jumpPower = 0;
                    gravity = isFlying() ? 1 : 0;
                }
                break;
            }
            didCollideY = false;
            if (dyabs / dy == 1 && state == State.MOVING) {
                jumpPower = 0;
                anim.changeAnimation(isFull ? "fulljump" : "jump");
                state = State.JUMPING;
            }
            y += dyabs / dy;
        }
        if (!isFlying() && !isJumping()) if (dx == 0 && state != State.NOT_MOVING) state = State.STOPPED_MOVING; else if (dx != 0 && state != State.MOVING) state = State.STARTED_MOVING;
        if (!isFlying()) gravity += .25;
        if (isFull) gravity += .125;
        switch(state) {
            case STARTED_MOVING:
                anim.changeAnimation(isFull ? "fullwalk" : "walk");
                state = State.MOVING;
                break;
            case STOPPED_MOVING:
                anim.changeAnimation(isFull ? "fullstand" : "stand");
                state = State.NOT_MOVING;
                break;
            case STARTED_JUMPING:
                anim.changeAnimation(isFull ? "fulljump" : "jump");
                state = State.JUMPING;
                break;
            case STARTED_FLYING:
                anim.changeAnimation("fly");
                state = State.FLYING;
                break;
            case STOPPED_FLYING:
                if (fallPower == 10) anim.changeAnimation("endfly");
                if (fallPower == 0) state = State.STARTED_JUMPING;
        }
        if (state == State.STOPPED_FLYING) fallPower--; else fallPower = 10;
    }

    public void draw() {
        double oldscale = Render.getScale();
        Render.addScale(2);
        anim.draw(x, y, flipped);
        Render.setScale(oldscale);
    }

    public boolean isDead() {
        return state == State.DEAD;
    }

    private boolean isFlying() {
        return state == State.STARTED_FLYING || state == State.FLYING;
    }

    private boolean isJumping() {
        return state == State.STARTED_JUMPING || state == State.JUMPING || state == State.STOPPED_FLYING;
    }

    private boolean isDying() {
        return state == State.STARTED_DYING || state == State.DYING;
    }

    private boolean isSucking() {
        return state == State.STARTED_SUCKING || state == State.SUCKING;
    }

    @Override
    public String save() {
        return String.format("%d %d %d %.2f %s %s", x, y, jumpPower, gravity, flipped, state.name());
    }

    @Override
    public void doCollide(GameObject obj) {
        if (obj instanceof Enemy) HUD.setLastEnemy((Enemy) obj);
        if (isSucking() && obj.isEdible()) {
            int mx = flipped ? x + 16 : x;
            if (obj.x >= mx && obj.x <= mx + 16) {
                for (Projectile projectile : projectiles) if (projectile instanceof Projectile.Sucking) projectile.die();
                state = State.NOT_MOVING;
                anim.changeAnimation("stand");
                health -= 30;
            }
            return;
        }
        if (!(obj instanceof Enemy)) return;
        if (invinciblePower > 0) obj.die(); else health--;
    }

    public boolean isEdible() {
        return false;
    }

    @LuaFunction
    public int getHealth() {
        return health;
    }

    @LuaFunction
    public int getMaxHealth() {
        return maxHealth;
    }

    public void die() {
        if (isDying() || isDead()) return;
        state = State.STARTED_DYING;
        gravity = -4;
        deathPower = 15;
        health = 0;
        SoundSystem.changeTrack(40);
    }

    public void setProjectiles(ArrayList<Projectile> p) {
        projectiles = p;
    }

    @LuaFunction
    public void setHealth(int h) {
        health = h;
        if (health > maxHealth) health = maxHealth;
    }

    @LuaFunction
    public void heal(int healz) {
        health += healz;
        if (health > maxHealth) health = maxHealth;
    }

    @LuaFunction
    public void gainMaxHealth(int gain) {
        health += gain;
        maxHealth += gain;
    }

    @LuaFunction
    public void addInvincible(int awesome) {
        int tmp = invinciblePower;
        invinciblePower += awesome;
        if (invinciblePower < 0) invinciblePower = 0;
        if (tmp == 0 && invinciblePower > 0) {
            SoundSystem.playTempMusic(3);
        }
    }

    private void createCollisParticle() {
        Particle p = new Particle();
        p.x = x + 16;
        p.y = y + 16;
        p.sprite = particleSprite;
        double rot = Math.random() * 360;
        p.dx = 4 * Math.cos(Math.toRadians(rot));
        p.dy = 4 * Math.sin(Math.toRadians(rot));
        p.mult = 4;
        p.state = 1;
        p.rot = rot;
        p.scale = 1;
        if (p.rot < 0) p.rot += 360;
        ParticleSystem.add(p);
    }

    private void createDeathParticle(double rot) {
        Particle p = new Particle();
        p.x = x + 16;
        p.y = y + 16;
        p.sprite = particleSprite;
        p.dx = 7 * Math.cos(Math.toRadians(rot));
        p.dy = 7 * Math.sin(Math.toRadians(rot));
        p.mult = 7;
        p.state = 3;
        p.rot = rot;
        p.scale = 1;
        if (p.rot < 0) p.rot += 360;
        ParticleSystem.add(p);
    }

    private void createSuckingParticle() {
        Particle p = new Particle();
        p.x = x + (flipped ? 0 - 32 : 64);
        p.y = y + (int) (Math.random() * 64) - 16;
        p.sprite = particle2Sprite;
        p.scale = 1;
        p.flipped = flipped;
        p.flipped2 = p.y - (y + 16) > 0;
        p.state = 2;
        if (p.rot < 0) p.rot += 360;
        ParticleSystem.add(p);
    }

    private enum State {

        NOT_MOVING, STARTED_MOVING, MOVING, STOPPED_MOVING, STARTED_JUMPING, JUMPING, STOPPED_JUMPING, STARTED_FLYING, FLYING, STOPPED_FLYING, STARTED_SUCKING, SUCKING, STOPPED_SUCKING, STOPPED_SUCKING2, SWALLOWING, SPITTING_OUT, STARTED_DYING, DYING, DEAD
    }

    private class Particle extends ParticleSystem.Particle {

        int state = 0;

        double mult = 0;

        @Override
        public void update0() {
            switch(state) {
                case 1:
                case 3:
                    updateCollisDeath();
                    break;
                case 2:
                    updateSuck();
            }
        }

        public void updateCollisDeath() {
            if (mult <= 0) isDead = true;
            mult -= .25;
            dx = mult * Math.cos(Math.toRadians(rot));
            dy = mult * Math.sin(Math.toRadians(rot));
        }

        double ddx = 1;

        boolean flipped, flipped2;

        public void updateSuck() {
            int kx = Kirby.this.x + (Kirby.this.w == 64 && flipped ? 32 : 16);
            int ky = Kirby.this.y + 16;
            boolean movedX = false, movedY = false;
            if ((!flipped && x > kx) || (flipped && x < kx)) {
                x += (flipped ? 1 : -1) * ddx;
                movedX = true;
            }
            if ((!flipped2 && y < ky) || (flipped2 && y > ky)) {
                y += (flipped2 ? -1 : 1) * (Math.random() < .5 ? 2 : Math.random() < .5 ? 3 : 1);
                movedY = true;
            }
            if (!movedX && !movedY) isDead = true;
            ddx *= 1.25;
        }
    }
}
