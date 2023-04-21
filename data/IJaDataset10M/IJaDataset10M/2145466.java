package com.organizadordeeventos.dto.servicio;

import com.organizadordeeventos.core.collection.list.ListaVersionable;
import com.organizadordeeventos.dto.multimedia.RecursoMultimediaDTO;

public interface ItemDePresupuestoDTO {

    public int getCodigo();

    public ListaVersionable<RecursoMultimediaDTO> getFotos();

    public void setFotos(ListaVersionable<RecursoMultimediaDTO> fotos);

    public ListaVersionable<RecursoMultimediaDTO> getVideos();

    public void setVideos(ListaVersionable<RecursoMultimediaDTO> videos);
}
