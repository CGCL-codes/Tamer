package net.sourceforge.symba.mapping.hibernatejaxb2.helper;

import net.sourceforge.fuge.bio.material.GenericMaterial;
import net.sourceforge.fuge.bio.material.Material;
import net.sourceforge.fuge.collection.MaterialCollection;
import net.sourceforge.fuge.service.EntityServiceException;
import net.sourceforge.fuge.util.generatedJAXB2.*;
import net.sourceforge.fuge.common.audit.Person;
import net.sourceforge.symba.mapping.hibernatejaxb2.DatabaseObjectHelper;
import javax.xml.bind.JAXBElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Copyright Notice
 *
 * The MIT License
 *
 * Copyright (c) 2008 2007-8 Proteomics Standards Initiative / Microarray and Gene Expression Data Society
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Acknowledgements
 *  The authors wish to thank the Proteomics Standards Initiative for
 *  the provision of infrastructure and expertise in the form of the PSI
 *  Document Process that has been used to formalise this document.
 * <p/>
 * $LastChangedDate: 2008-08-06 05:43:17 -0400 (Wed, 06 Aug 2008) $
 * $LastChangedRevision: 194 $
 * $Author: allysonlister $
 * $HeadURL: http://symba.svn.sourceforge.net/svnroot/symba/tags/release-8.09/symba-mapping/src/main/java/net/sourceforge/symba/mapping/hibernatejaxb2/helper/MaterialCollectionMappingHelper.java $
 */
public class MaterialCollectionMappingHelper implements MappingHelper<MaterialCollection, FuGECollectionMaterialCollectionType> {

    private final DescribableMappingHelper cd;

    private final MaterialMappingHelper cm;

    public MaterialCollectionMappingHelper() {
        this.cd = (new IdentifiableMappingHelper()).getCisbanDescribableHelper();
        this.cm = new MaterialMappingHelper();
    }

    public MaterialCollection unmarshal(FuGECollectionMaterialCollectionType matCollXML, MaterialCollection matColl, Person performer) throws EntityServiceException {
        matColl = (MaterialCollection) cd.unmarshal(matCollXML, matColl, performer);
        matColl = unmarshalCollectionContents(matCollXML, matColl, performer);
        DatabaseObjectHelper.save("net.sourceforge.fuge.collection.MaterialCollection", matColl, performer);
        return matColl;
    }

    public MaterialCollection unmarshalCollectionContents(FuGECollectionMaterialCollectionType matCollXML, MaterialCollection matColl, Person performer) throws EntityServiceException {
        Set<Material> materials = new HashSet<Material>();
        for (JAXBElement<? extends FuGEBioMaterialMaterialType> materialElementXML : matCollXML.getMaterial()) {
            FuGEBioMaterialMaterialType materialXML = materialElementXML.getValue();
            if (materialXML instanceof FuGEBioMaterialGenericMaterialType) {
                GenericMaterial gmaterial = (GenericMaterial) DatabaseObjectHelper.getOrCreate(materialXML.getIdentifier(), materialXML.getEndurantRef(), materialXML.getName(), "net.sourceforge.fuge.bio.material.GenericMaterial");
                gmaterial = (GenericMaterial) cm.unmarshal(materialXML, gmaterial, performer);
                DatabaseObjectHelper.save("net.sourceforge.fuge.bio.material.GenericMaterial", gmaterial, performer);
                materials.add(gmaterial);
            }
        }
        matColl.setMaterials(materials);
        return matColl;
    }

    public FuGECollectionMaterialCollectionType marshal(FuGECollectionMaterialCollectionType matCollXML, MaterialCollection matColl) throws EntityServiceException {
        matCollXML = (FuGECollectionMaterialCollectionType) cd.marshal(matCollXML, matColl);
        ObjectFactory factory = new ObjectFactory();
        for (Material material : matColl.getMaterials()) {
            if (material instanceof GenericMaterial) {
                matCollXML.getMaterial().add(factory.createGenericMaterial((FuGEBioMaterialGenericMaterialType) cm.marshal(new FuGEBioMaterialGenericMaterialType(), material)));
            }
        }
        return matCollXML;
    }
}
