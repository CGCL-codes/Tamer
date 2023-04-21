package org.authorsite.bib.ejb.services.dto;

import java.lang.reflect.*;
import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import org.authorsite.bib.ejb.entity.*;
import org.authorsite.bib.dto.*;

/**
 *
 * @author  jejking
 * @version $Revision: 1.1 $
 */
public class MediaItemDTOAssembler {

    private MediaItemLocal myMediaItemLocal;

    private MediaItemDTO myMediaItemDTO;

    public MediaItemDTOAssembler(MediaItemLocal newMediaItemLocal) {
        myMediaItemLocal = newMediaItemLocal;
        myMediaItemDTO = new MediaItemDTO(myMediaItemLocal.getMediaItemID());
    }

    public MediaItemDTO assembleDTO() {
        myMediaItemDTO.setTitle(myMediaItemLocal.getTitle());
        myMediaItemDTO.setMediaType(myMediaItemLocal.getMediaType());
        myMediaItemDTO.setYearOfCreation(myMediaItemLocal.getYearOfCreation());
        myMediaItemDTO.setPublishedFlag(myMediaItemLocal.getPublishedFlag());
        myMediaItemDTO.setAdditionalInfo(myMediaItemLocal.getAdditionalInfo());
        myMediaItemDTO.setComment(myMediaItemLocal.getComment());
        Set languages = myMediaItemLocal.getLanguages();
        Set myLanguageDTOs = new HashSet();
        Iterator languagesIt = languages.iterator();
        while (languagesIt.hasNext()) {
            LanguageLocal currentLang = (LanguageLocal) languagesIt.next();
            LanguageDTOAssembler languageDTOAssembler = new LanguageDTOAssembler(currentLang);
            myLanguageDTOs.add(languageDTOAssembler.assembleDTO());
        }
        myMediaItemDTO.setLanguages(myLanguageDTOs);
        Set mediaProductionRelationships = myMediaItemLocal.getMediaProductionRelationships();
        Set myMediaProductionRelationshipDTOs = new HashSet();
        Iterator prodRelsIt = mediaProductionRelationships.iterator();
        while (prodRelsIt.hasNext()) {
            MediaProductionRelationshipLocal currentProdRel = (MediaProductionRelationshipLocal) prodRelsIt.next();
            myMediaProductionRelationshipDTOs.add(new MediaProductionRelationshipDTOAssembler(currentProdRel).assembleDTO());
        }
        myMediaItemDTO.setMediaProductionRelationships(myMediaProductionRelationshipDTOs);
        handleDetailsObject();
        Set mediaItemContainerSet = myMediaItemLocal.getContainer();
        if (!mediaItemContainerSet.isEmpty()) {
            Iterator containerIt = mediaItemContainerSet.iterator();
            MediaItemLocal container = (MediaItemLocal) containerIt.next();
            MediaItemDTOAssembler containerAssembler = new MediaItemDTOAssembler(container);
            myMediaItemDTO.setContainedIn(containerAssembler.assembleDTO());
        }
        Collection childrenIMRs = myMediaItemLocal.getChildIntraMediaRelationships();
        if (childrenIMRs.size() > 0) {
            HashMap childrenMap = new HashMap();
            Iterator childrenTypesIt = childrenIMRs.iterator();
            while (childrenTypesIt.hasNext()) {
                String typeKey = childrenTypesIt.next().toString();
                Collection childrenOfType = myMediaItemLocal.getAllChildrenOfType(typeKey);
                ArrayList childrenDTOsOfTypeList = new ArrayList();
                Iterator childrenOfTypeIt = childrenOfType.iterator();
                while (childrenOfTypeIt.hasNext()) {
                    MediaItemLocal childItem = (MediaItemLocal) childrenOfTypeIt.next();
                    MediaItemDTOAssembler assembler = new MediaItemDTOAssembler(childItem);
                    childrenDTOsOfTypeList.add(assembler.assembleLightweightDTO());
                }
                childrenMap.put(typeKey, childrenDTOsOfTypeList);
            }
            myMediaItemDTO.setChildren(childrenMap);
        }
        Collection parentsIMRs = myMediaItemLocal.getParentIntraMediaRelationships();
        if (parentsIMRs.size() > 0) {
            HashMap parentsMap = new HashMap();
            Iterator parentsTypesIt = parentsIMRs.iterator();
            while (parentsTypesIt.hasNext()) {
                String typeKey = parentsTypesIt.next().toString();
                Collection parentsOfType = myMediaItemLocal.getPublishedParentsOfType(typeKey);
                ArrayList parentsDTOsOfTypeList = new ArrayList();
                Iterator parentsOfTypeIt = parentsOfType.iterator();
                while (parentsOfTypeIt.hasNext()) {
                    MediaItemLocal parentItem = (MediaItemLocal) parentsOfTypeIt.next();
                    MediaItemDTOAssembler assembler = new MediaItemDTOAssembler(parentItem);
                    parentsDTOsOfTypeList.add(assembler.assembleLightweightDTO());
                }
                parentsMap.put(typeKey, parentsDTOsOfTypeList);
            }
            myMediaItemDTO.setParents(parentsMap);
        }
        return myMediaItemDTO;
    }

    public MediaItemDTO assembleLightweightDTO() {
        myMediaItemDTO.setTitle(myMediaItemLocal.getTitle());
        myMediaItemDTO.setMediaType(myMediaItemLocal.getMediaType());
        myMediaItemDTO.setYearOfCreation(myMediaItemLocal.getYearOfCreation());
        myMediaItemDTO.setPublishedFlag(myMediaItemLocal.getPublishedFlag());
        return myMediaItemDTO;
    }

    private void handleDetailsObject() {
        if (myMediaItemDTO.getMediaType() == null) {
            return;
        }
        try {
            String DTOFactoryName = "org.authorsite.bib.ejb.services.dto." + capitaliseFirstLetter(myMediaItemDTO.getMediaType()) + "DetailsDTOFactory";
            Class dtoFactoryClass = Class.forName(DTOFactoryName);
            Class constructorParams[] = new Class[1];
            constructorParams[0] = Integer.class;
            Constructor dtoFactoryClassConstructor = dtoFactoryClass.getConstructor(constructorParams);
            Object args[] = new Object[1];
            args[0] = myMediaItemDTO.getID();
            MediaItemDetailsDTOFactory factory = (MediaItemDetailsDTOFactory) dtoFactoryClassConstructor.newInstance(args);
            MediaItemDetailsDTO detailsDTO = factory.getDTO();
            if (detailsDTO == null) {
                return;
            } else {
                myMediaItemDTO.setDetailsDTO(detailsDTO);
            }
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return;
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return;
        } catch (InstantiationException ie) {
            ie.printStackTrace();
            return;
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            return;
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
            return;
        }
    }

    private String constructJNDILookUpString(String baseMediaType) {
        String jndiLookUp = "ejb/" + capitaliseFirstLetter(baseMediaType) + "DetailsLocalEJB";
        return jndiLookUp;
    }

    private String capitaliseFirstLetter(String capitaliseThisString) {
        char[] chars = capitaliseThisString.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
