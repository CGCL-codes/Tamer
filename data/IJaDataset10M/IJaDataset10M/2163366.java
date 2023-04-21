package org.fao.fenix.domain.info.pattern.cropproduction;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.info.content.Content;
import org.fao.fenix.domain.info.dataset.MeasurementOfUnitDataset;
import org.fao.fenix.domain.info.pattern.cropproduction.content.CropAreaAgriculturalPractice;

@Entity
public class CropAreaDataset extends MeasurementOfUnitDataset {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CropAreaAgriculturalPractice> contentList;

    @Override
    public void addContent(Content cropAreaAgriculturalPractice) {
        if (!(cropAreaAgriculturalPractice instanceof CropAreaAgriculturalPractice)) throw new FenixException("Only CropAreaAgriculturalPractice content allowed here");
        if (contentList == null) {
            contentList = new ArrayList<CropAreaAgriculturalPractice>();
        }
        this.contentList.add((CropAreaAgriculturalPractice) cropAreaAgriculturalPractice);
    }

    @Override
    public List<CropAreaAgriculturalPractice> getContentList() {
        return this.contentList;
    }
}
