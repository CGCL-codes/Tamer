package fr.ign.cogit.geoxygene.spatial.geomprim;

import java.util.Set;
import fr.ign.cogit.geoxygene.api.spatial.geomcomp.IComplex;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPrimitive;
import fr.ign.cogit.geoxygene.spatial.geomroot.GM_Object;

/**
 * Classe mère abstraite pour les primitives géométriques (point, ligne,
 * surface, solide). Son but est définir l'opération de base "boundary()" qui
 * lie les primitives de différentes dimensions entre elles. Cette opération est
 * redéfinie dans les sous-classes concrètes pour assurer un bon typage. Une
 * primitive géométrique ne peut pas être décomposée en autres primitives, même
 * si elle découpée en morceaux de courbes (curve segment) ou en morceaux de
 * surface (surface patch) : un curve segment et un surface patch ne peuvent pas
 * exister en dehors du contexte d'une primitive. GM_Complex et GM_Primitive
 * partagent les mêmes propriétés, sauf qu'un complexe est fermé par l'opération
 * "boundary". Par exemple pour une CompositeCurve,
 * GM_Primitive::contains(endPoint) retourne FALSE, alors que
 * GM_Complex::contains(endPoint) retourne TRUE. En tant que GM_Object ces 2
 * objets seront égaux.
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 * 
 */
public abstract class GM_Primitive extends GM_Object implements IPrimitive {

    @Override
    public Set<IComplex> getComplex() {
        return null;
    }

    @Override
    public int sizeComplex() {
        return 0;
    }
}
