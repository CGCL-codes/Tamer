package org.nvframe.component.render;

import java.io.IOException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.nvframe.component.state.Position;
import org.nvframe.entity.Entity;
import org.nvframe.event.EventService;
import org.nvframe.event.eventtype.RenderEvent;
import org.nvframe.event.eventtype.RenderListener;
import org.nvframe.event.eventtype.UpdateEvent;
import org.nvframe.event.eventtype.UpdateListener;
import org.nvframe.exception.NVFrameException;
import org.nvframe.manager.ResourceManager;
import org.nvframe.util.ParticleResource;
import org.nvframe.util.settings.SettingsObj;

public class ParticleRender extends AbstractRender implements UpdateListener, RenderListener {

    private ParticleSystem particleSystem;

    public ParticleRender(String id, Entity owner, SettingsObj settings) throws NVFrameException {
        super(id, owner, settings);
        loadParticleSystem(settings);
        EventService.getInstance().addEventListener(this);
    }

    /**
	 * Get the configured Image resource id from Settings and 
	 * get a reference to the Image resource trough ResourceManager
	 * 
	 * @param settings The Settings
	 * @throws NVFrameException
	 */
    public void loadParticleSystem(SettingsObj settings) throws NVFrameException {
        String resource = settings.getString("particleResource");
        try {
            ParticleResource particleResource = ResourceManager.getInstance().getParticleResource(resource);
            particleSystem = ParticleIO.loadConfiguredSystem(particleResource.getParticleSystem());
            particleSystem.setDefaultImageName(particleResource.getImage());
        } catch (NVFrameException e) {
            throw new NVFrameException("cannot load ParticleRender component [id:" + resource + "]", e);
        } catch (IOException e) {
            throw new NVFrameException("cannot load ParticleRender component [id:" + resource + "]", e);
        }
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        Position position = (Position) owner.getComponent(Position.class);
        if (position == null) return;
        particleSystem.setPosition(position.getX(), position.getY());
        particleSystem.update(event.getDelta());
    }

    @Override
    public void onRender(RenderEvent event) {
        particleSystem.render();
    }
}
