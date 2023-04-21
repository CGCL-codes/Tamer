package com.loribel.commons.business.metamodel;

import com.loribel.commons.abstraction.GB_IdOwner;
import com.loribel.commons.abstraction.GB_ItemsOwner;
import com.loribel.commons.business.abstraction.GB_BOMetaDataContainer;
import com.loribel.commons.business.impl.bo.GB_BOMetaDataBO;

/**
 * Abstraction of MetaData container.
 * Par exemple l'impl�mentation GB_BOBOMetaDataContainerFile permet de relier ensemble tous
 * les BOMetaData issus d'un fichier. De plus, � partir du BOMetaData, on peut retrouver le Conteneur
 * si c'est ce dernier qui l'a cr�e.
 *
 * @author Gregory Borelli
 */
public interface GB_BOBOMetaDataContainer extends GB_BOMetaDataContainer, GB_IdOwner, GB_ItemsOwner {

    GB_BOMetaDataBO getMetaDataBO(String a_boName);
}
