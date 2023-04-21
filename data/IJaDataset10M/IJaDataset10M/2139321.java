package es.eucm.eadventure.editor.gui.structurepanel.structureelements.Effects;

import es.eucm.eadventure.common.gui.TC;

public class ChangesInSceneStructureListElement extends EffectsStructureListElement {

    private static final String LIST_URL = "effects_short/Effects_SceneChanges.html";

    public ChangesInSceneStructureListElement() {
        super(TC.get("EffectsGroup.ChangeInScene"));
        groupEffects = new String[] { TC.get("Effect.ConsumeObject"), TC.get("Effect.GenerateObject"), TC.get("Effect.MovePlayer"), TC.get("Effect.MoveCharacter"), TC.get("Effect.HighlightItem"), TC.get("Effect.MoveObject") };
        path = LIST_URL;
    }
}
