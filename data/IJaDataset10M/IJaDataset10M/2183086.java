package fr.ign.cogit.geoxygene.spatial.geomprim;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IBoundary;
import fr.ign.cogit.geoxygene.spatial.geomcomp.GM_Complex;

/**
 * Classe mère abstraite pour tous les types représentant les frontières des
 * objets géométriques. Toute sous-classe de GM_Object utilisera une sous-classe
 * de GM_Boundary pour représenter sa frontière via l'opération
 * GM_Object::boundary(). Par nature, les GM_Boundary sont des cycles.
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 */
public abstract class GM_Boundary extends GM_Complex implements IBoundary {
}
