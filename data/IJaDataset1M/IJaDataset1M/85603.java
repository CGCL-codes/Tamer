package net.sourceforge.wildlife.core.components.food;

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import net.sourceforge.wildlife.core.components.actor.Food;
import net.sourceforge.wildlife.core.environment.World;
import net.sourceforge.wildlife.core.types.Actor_status;

/**
 * Nectar class
 * 
 * @author Jean Barata
 */
public class Nectar extends Food {

    /**
	 * Nectar class constructor
	 * 
	 * @param world_p
	 */
    public Nectar(World world_p) {
        super(world_p);
        set_nutritive_value(10);
        set_validity_duration(-1);
    }

    /**
	 * Nectar class constructor
	 * 
	 * @param threadGroup_p
	 * @param world_p
	 */
    public Nectar(ThreadGroup threadGroup_p, World world_p) {
        super(threadGroup_p, world_p);
        set_nutritive_value(10);
        set_validity_duration(-1);
    }

    /**
	 *
	 */
    @Override
    public String get_type() {
        return "NECTAR";
    }

    ;

    /**
	 *@param state_p
	 *@return
	 */
    @Override
    public URL get_image() {
        IPath path = new Path("icons/nectar.gif");
        return FileLocator.find(NectarPlugin.getDefault().getBundle(), path, null);
    }

    /**
	 *
	 */
    @Override
    public void run() {
        try {
            while (get_status() != Actor_status.DEAD) {
                if (get_validity_duration() > 0) {
                    set_nutritive_value(get_nutritive_value() - 1);
                    set_validity_duration(get_validity_duration() - 1);
                }
                Thread.sleep(1000);
                if (get_validity_duration() == 0) set_status(Actor_status.DEAD);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
