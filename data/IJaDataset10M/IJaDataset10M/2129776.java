package com.tesisutn.restsoft.formatters.factoryBean;

import javax.annotation.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Service;
import com.tesisutn.restsoft.dominio.articulo.*;
import com.tesisutn.restsoft.dominio.datos.TipoDeDocumento;
import com.tesisutn.restsoft.formatters.*;

@Service
public class MyFormattingFactory extends FormattingConversionServiceFactoryBean {

    @Resource
    private LugarDePreparacionFormatter lugarDePreparacionFormatter;

    @Resource
    private MarcaFormatter marcaFormatter;

    @Resource
    private PresentacionFormatter presentacionFormatter;

    @Resource
    private SubrubroFormatter subrubroFormatter;

    @Resource
    private TipoDeDocumentoFormatter tipoDeDocumentoFormatter;

    @Resource
    private ArticuloCompradoFormatter articuloCompradoFormatter;

    @Override
    protected void installFormatters(FormatterRegistry registry) {
        super.installFormatters(registry);
        registry.addFormatterForFieldType(LugarDePreparacion.class, lugarDePreparacionFormatter);
        registry.addFormatterForFieldType(Marca.class, marcaFormatter);
        registry.addFormatterForFieldType(Presentacion.class, presentacionFormatter);
        registry.addFormatterForFieldType(Subrubro.class, subrubroFormatter);
        registry.addFormatterForFieldType(TipoDeDocumento.class, tipoDeDocumentoFormatter);
        registry.addFormatterForFieldType(ArticuloComprado.class, articuloCompradoFormatter);
    }
}
