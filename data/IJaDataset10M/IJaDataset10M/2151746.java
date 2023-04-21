package ar.com.fdvs.bean2bean.inv;

import ar.com.fdvs.bean2bean.annotations.CopyTo;
import ar.com.fdvs.bean2bean.annotations.FormatterPattern;
import ar.com.fdvs.bean2bean.annotations.MissingPropertyAction;
import ar.com.fdvs.bean2bean.interpreters.InterpreterType;
import ar.com.fdvs.dgarcia.testing.Assert;

/**
 * Esta clase prueba las distintas formas de populacion tomando los datos desde esta instancia y
 * asignandosea a otra
 * 
 * @author D.Garcia
 */
public class ClasePopuladora {

    @CopyTo
    private String propiedadPrimitiva;

    @CopyTo("copiadaDesdeOtroNombre")
    private String copiadoHaciaOtroNombre;

    @CopyTo("atributoPublico")
    private String copiadoHaciaUnaPropiedadPublica;

    @CopyTo("atributoPrivado")
    private String copiadoHaciaUnaPropiedadPrivada;

    @CopyTo(value = "propiedadAnidadaInstanciada.propiedadPrimitiva", expectedType = "java.lang.String")
    private String copiadoHaciaUnaPropiedadAnidada;

    @CopyTo(value = "asignadoConOgnl", setterInterpreter = InterpreterType.OGNL)
    private String usandoOgnlEnDestino;

    @CopyTo(value = "_destino.asignadoConGroovy = _valor", setterInterpreter = InterpreterType.GROOVY)
    private String usandoGroovyEnDestino;

    @CopyTo(value = "obtenidoConOgnl", getter = "usandoOgnlEnObtencion", getterInterpreter = InterpreterType.OGNL)
    private String usandoOgnlEnObtencion;

    @CopyTo(value = "obtenidoConGroovy", getter = "usandoGroovyEnObtencion", getterInterpreter = InterpreterType.GROOVY)
    private String usandoGroovyEnObtencion;

    @CopyTo(value = "obtenidoYAsignadoConOgnl", setterInterpreter = InterpreterType.OGNL, getter = "usandoOgnlParaObtenerYAsignar", getterInterpreter = InterpreterType.OGNL)
    private String usandoOgnlParaObtenerYAsignar;

    @CopyTo(value = "_destino.obtenidoYAsignadoConGroovy = _valor", setterInterpreter = InterpreterType.GROOVY, getter = "usandoGroovyParaObtenerYAsignar", getterInterpreter = InterpreterType.GROOVY)
    private String usandoGroovyParaObtenerYAsignar;

    @CopyTo(value = "obtencionConGroovyYasignacionOgnl", setterInterpreter = InterpreterType.OGNL, getter = "_origen.usandoGroovyParaObtenerYOgnlParaAsignar", getterInterpreter = InterpreterType.GROOVY)
    private String usandoGroovyParaObtenerYOgnlParaAsignar;

    @CopyTo(value = "propiedadInexistente", whenMissing = MissingPropertyAction.TREAT_AS_NULL)
    private String silenciosoSiNoExistePropiedad;

    @CopyTo(value = "propiedadAnidada.propiedadAnidada.propiedadPrimitiva", whenMissing = MissingPropertyAction.CREATE_MISSING_INSTANCES)
    private String generandoPathDePropiedades;

    @CopyTo(value = "longConOgnl", expectedType = "@java.lang.Long@class", typeInterpreter = InterpreterType.OGNL)
    private Number especificandoElTipoConOgnl;

    @CopyTo(value = "longConGroovy", expectedType = "Long.class", typeInterpreter = InterpreterType.GROOVY)
    private Number especificandoElTipoConGroovy;

    @CopyTo(value = "conApostrofes", useConversor = "ar.com.fdvs.bean2bean.conversion.converters.FormatterConverter", contextAnnotations = FormatterPattern.class)
    @FormatterPattern("'%s'")
    private String especificandoFormato;

    @CopyTo("propiedadAnidadaInstanciada.fromSimpleString")
    private String toNestedProperty;

    public String getPropiedadPrimitiva() {
        return propiedadPrimitiva;
    }

    public void setPropiedadPrimitiva(String propiedadPrimitiva) {
        this.propiedadPrimitiva = propiedadPrimitiva;
    }

    public String getCopiadoHaciaOtroNombre() {
        return copiadoHaciaOtroNombre;
    }

    public void setCopiadoHaciaOtroNombre(String copiadoHaciaOtroNombre) {
        this.copiadoHaciaOtroNombre = copiadoHaciaOtroNombre;
    }

    public String getCopiadoHaciaUnaPropiedadPublica() {
        return copiadoHaciaUnaPropiedadPublica;
    }

    public void setCopiadoHaciaUnaPropiedadPublica(String copiadoHaciaUnaPropiedadPublica) {
        this.copiadoHaciaUnaPropiedadPublica = copiadoHaciaUnaPropiedadPublica;
    }

    public String getCopiadoHaciaUnaPropiedadPrivada() {
        return copiadoHaciaUnaPropiedadPrivada;
    }

    public void setCopiadoHaciaUnaPropiedadPrivada(String copiadoHaciaUnaPropiedadPrivada) {
        this.copiadoHaciaUnaPropiedadPrivada = copiadoHaciaUnaPropiedadPrivada;
    }

    public String getUsandoOgnlEnDestino() {
        return usandoOgnlEnDestino;
    }

    public void setUsandoOgnlEnDestino(String usandoOgnlEnDestino) {
        this.usandoOgnlEnDestino = usandoOgnlEnDestino;
    }

    public String getUsandoGroovyEnDestino() {
        return usandoGroovyEnDestino;
    }

    public void setUsandoGroovyEnDestino(String usandoGroovyEnDestino) {
        this.usandoGroovyEnDestino = usandoGroovyEnDestino;
    }

    public String getUsandoOgnlEnObtencion() {
        return usandoOgnlEnObtencion;
    }

    public void setUsandoOgnlEnObtencion(String usandoOgnlEnObtencion) {
        this.usandoOgnlEnObtencion = usandoOgnlEnObtencion;
    }

    public String getUsandoGroovyEnObtencion() {
        return usandoGroovyEnObtencion;
    }

    public void setUsandoGroovyEnObtencion(String usandoGroovyEnObtencion) {
        this.usandoGroovyEnObtencion = usandoGroovyEnObtencion;
    }

    public String getUsandoOgnlParaObtenerYAsignar() {
        return usandoOgnlParaObtenerYAsignar;
    }

    public void setUsandoOgnlParaObtenerYAsignar(String usandoOgnlParaObtenerYAsignar) {
        this.usandoOgnlParaObtenerYAsignar = usandoOgnlParaObtenerYAsignar;
    }

    public String getUsandoGroovyParaObtenerYAsignar() {
        return usandoGroovyParaObtenerYAsignar;
    }

    public void setUsandoGroovyParaObtenerYAsignar(String usandoGroovyParaObtenerYAsignar) {
        this.usandoGroovyParaObtenerYAsignar = usandoGroovyParaObtenerYAsignar;
    }

    public String getUsandoGroovyParaObtenerYOgnlParaAsignar() {
        return usandoGroovyParaObtenerYOgnlParaAsignar;
    }

    public void setUsandoGroovyParaObtenerYOgnlParaAsignar(String usandoGroovyParaObtenerYOgnlParaAsignar) {
        this.usandoGroovyParaObtenerYOgnlParaAsignar = usandoGroovyParaObtenerYOgnlParaAsignar;
    }

    public String getSilenciosoSiNoExistePropiedad() {
        return silenciosoSiNoExistePropiedad;
    }

    public void setSilenciosoSiNoExistePropiedad(String silenciosoSiNoExistePropiedad) {
        this.silenciosoSiNoExistePropiedad = silenciosoSiNoExistePropiedad;
    }

    public String getGenerandoPathDePropiedades() {
        return generandoPathDePropiedades;
    }

    public void setGenerandoPathDePropiedades(String generandoPathDePropiedades) {
        this.generandoPathDePropiedades = generandoPathDePropiedades;
    }

    public Number getEspecificandoElTipoConOgnl() {
        return especificandoElTipoConOgnl;
    }

    public void setEspecificandoElTipoConOgnl(Number especificandoElTipoConOgnl) {
        this.especificandoElTipoConOgnl = especificandoElTipoConOgnl;
    }

    public Number getEspecificandoElTipoConGroovy() {
        return especificandoElTipoConGroovy;
    }

    public void setEspecificandoElTipoConGroovy(Number especificandoElTipoConGroovy) {
        this.especificandoElTipoConGroovy = especificandoElTipoConGroovy;
    }

    public String getEspecificandoFormato() {
        return especificandoFormato;
    }

    public void setEspecificandoFormato(String especificandoFormato) {
        this.especificandoFormato = especificandoFormato;
    }

    public static ClasePopuladora create() {
        ClasePopuladora populadora = new ClasePopuladora();
        populadora.setPropiedadPrimitiva("PropiedadPrimitiva");
        populadora.setCopiadoHaciaOtroNombre("CopiadoHaciaOtroNombre");
        populadora.setCopiadoHaciaUnaPropiedadPublica("CopiadoHaciaUnaPropiedadPublica");
        populadora.setCopiadoHaciaUnaPropiedadPrivada("CopiadoHaciaUnaPropiedadPrivada");
        populadora.setCopiadoHaciaUnaPropiedadAnidada("CopiadoHaciaUnaPropiedadAnidada");
        populadora.setUsandoOgnlEnDestino("UsandoOgnlEnDestino");
        populadora.setUsandoGroovyEnDestino("UsandoGroovyEnDestino");
        populadora.setUsandoOgnlEnObtencion("UsandoOgnlEnObtencion");
        populadora.setUsandoGroovyEnObtencion("UsandoGroovyEnObtencion");
        populadora.setUsandoOgnlParaObtenerYAsignar("UsandoOgnlParaObtenerYAsignar");
        populadora.setUsandoGroovyParaObtenerYAsignar("UsandoGroovyParaObtenerYAsignar");
        populadora.setUsandoGroovyParaObtenerYOgnlParaAsignar("UsandoGroovyParaObtenerYOgnlParaAsignar");
        populadora.setSilenciosoSiNoExistePropiedad("SilenciosoSiNoExistePropiedad");
        populadora.setGenerandoPathDePropiedades("GenerandoPathDePropiedades");
        populadora.setEspecificandoElTipoConOgnl(2.0);
        populadora.setEspecificandoElTipoConGroovy(3.0);
        populadora.setEspecificandoFormato("EspecificandoFormato");
        return populadora;
    }

    /**
	 * @param populada
	 */
    public void verificarContra(ClasePopulada populada) {
        Assert.equals(this.getPropiedadPrimitiva(), populada.getPropiedadPrimitiva());
        Assert.equals(this.getCopiadoHaciaOtroNombre(), populada.getCopiadaDesdeOtroNombre());
        Assert.equals(this.getCopiadoHaciaUnaPropiedadPublica(), populada.atributoPublico);
        Assert.equals(this.getCopiadoHaciaUnaPropiedadPrivada(), populada.getterAtributoPrivado());
        Assert.equals(this.getCopiadoHaciaUnaPropiedadAnidada(), populada.getPropiedadAnidadaInstanciada().getPropiedadPrimitiva());
        Assert.equals(this.getUsandoOgnlEnDestino(), populada.getAsignadoConOgnl());
        Assert.equals(this.getUsandoGroovyEnDestino(), populada.getAsignadoConGroovy());
        Assert.equals(this.getUsandoOgnlEnObtencion(), populada.getObtenidoConOgnl());
        Assert.equals(this.getUsandoGroovyEnObtencion(), populada.getObtenidoConGroovy());
        Assert.equals(this.getUsandoOgnlParaObtenerYAsignar(), populada.getObtenidoYAsignadoConOgnl());
        Assert.equals(this.getUsandoGroovyParaObtenerYAsignar(), populada.getObtenidoYAsignadoConGroovy());
        Assert.equals(this.getUsandoGroovyParaObtenerYOgnlParaAsignar(), populada.getObtencionConGroovyYasignacionOgnl());
        Assert.equals(Long.class, populada.getLongConOgnl().getClass());
        Assert.equals(Long.class, populada.getLongConGroovy().getClass());
        Assert.equals("'" + this.getEspecificandoFormato() + "'", populada.getConApostrofes());
        Assert.equals(this.getToNestedProperty(), populada.getPropiedadAnidadaInstanciada().getFromSimpleString());
    }

    public String getCopiadoHaciaUnaPropiedadAnidada() {
        return copiadoHaciaUnaPropiedadAnidada;
    }

    public void setCopiadoHaciaUnaPropiedadAnidada(String copiadoHaciaUnaPropiedadAnidada) {
        this.copiadoHaciaUnaPropiedadAnidada = copiadoHaciaUnaPropiedadAnidada;
    }

    public String getToNestedProperty() {
        return toNestedProperty;
    }

    public void setToNestedProperty(String toNestedProperty) {
        this.toNestedProperty = toNestedProperty;
    }
}
