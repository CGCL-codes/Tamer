package com.proyecto.bigbang.charts;

import java.util.List;
import com.proyecto.bigbang.core.reportes.filters.Filtro;

/**
 * Inteface para los charts que generan los reportes
 * @author diego
 *
 */
public interface ReporteChart {

    /**
	 * Muestra el chart generado. Si no fue asociado con 
	 * una lista muestra un mesaje de error.
	 * @author diego
	 */
    void showChart();

    /**
	 * Asocia la lista del reporte para que se pueda generar
	 * el grafico a partir de los datos en ella
	 * @param data. Lista del reporte
	 * @author diego
	 */
    void setData(List<?> data, Filtro filtro);
}
