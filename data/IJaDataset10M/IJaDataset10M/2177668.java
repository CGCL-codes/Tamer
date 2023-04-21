package com.gft.larozanam.shared.listgriddatasources;

import com.gft.larozanam.client.componentes.dados.GridDataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class EnfermagemIdosoDataSource extends GridDataSource {

    public DataSourceField[] createColumns() {
        DataSourceIntegerField pk = new DataSourceIntegerField("codigo");
        pk.setHidden(true);
        pk.setPrimaryKey(true);
        return new DataSourceField[] { pk, new DataSourceDateField("dataInternacao", "Data Internação"), new DataSourceIntegerField("quarto", "Quarto"), new DataSourceIntegerField("leito", "Leito"), new DataSourceIntegerField("peso", "Peso"), new DataSourceTextField("habitos", "Hábitos"), new DataSourceTextField("motivoInternacao", "Motivo Internação"), new DataSourceTextField("procedencia", "Procedência"), new DataSourceTextField("etnia", "Etnia"), new DataSourceTextField("medicamentos", "Medicamentos"), new DataSourceBooleanField("pressaoAlta", "Pressão Alta"), new DataSourceBooleanField("fumante", "Fumante"), new DataSourceBooleanField("independente", "Independente") };
    }

    @Override
    protected String injetarServico() {
        return "enfermagemIdosoBusiness";
    }
}
