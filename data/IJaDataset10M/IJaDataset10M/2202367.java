package logic.nodes;

import main.InitGame;
import interfaces.hud.spectate.SpectateHUD;
import interfaces.hud.targetInfos.TargetInfoHandler;
import com.jme.input.MouseInput;
import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.system.DisplaySystem;

public class MouseHoverController extends Controller {

    private static final long serialVersionUID = 1L;

    private TeamModelNode node;

    private PickResults pickResults;

    public MouseHoverController(TeamModelNode node) {
        this.node = node;
        pickResults = new BoundingPickResults();
        TargetInfoHandler.getInstance().registerNewNode(node);
    }

    @Override
    public void update(float time) {
        if (!TargetInfoHandler.getInstance().isInitialized() || InitGame.getHUDState() == null) return;
        if (InitGame.getHUDState().getHUD() instanceof SpectateHUD && node == ((SpectateHUD) InitGame.getHUDState().getHUD()).getWatchedPlayer().getHunter()) return;
        MouseInput mouse = MouseInput.get();
        Vector2f screenPos = new Vector2f(mouse.getXAbsolute(), mouse.getYAbsolute());
        DisplaySystem display = DisplaySystem.getDisplaySystem();
        Vector3f worldCoords = display.getWorldCoordinates(screenPos, 0);
        Vector3f worldCoords2 = display.getWorldCoordinates(screenPos, 1);
        Ray mouseRay = new Ray(worldCoords, worldCoords2.subtractLocal(worldCoords).normalizeLocal());
        pickResults.clear();
        node.findPick(mouseRay, pickResults);
        if (pickResults.getNumber() > 0 && !node.isHovered()) node.setHovered(true); else if (pickResults.getNumber() == 0 && node.isHovered()) node.setHovered(false);
    }
}
