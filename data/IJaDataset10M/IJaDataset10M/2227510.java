package neo.bcb;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.util.*;
import javax.xml.stream.*;
import org.apache.axis.AxisFault;

/**
 * Classe intermediadora entre o web service Sistema Gerenciador de Séries Temporais
 * do Banco Central do Brasil (aka SGS do BCB) e aplicativos clientes, extraindo dados
 * das séries temporais requisitadas.
*/
public class Aurea {

    /** Stub do webservice. */
    public FachadaWSSGSSoapBindingStub stub;

    /** Client side do webservice. */
    private FachadaWSSGSServiceLocator service;

    /** Pointer para único factory de StAX Parsers. */
    private XMLInputFactory inputFactory;

    /** Construtor de objetos desta classe. */
    protected Aurea() {
        service = new FachadaWSSGSServiceLocator();
        String tm = System.getProperty("webservice.timeout");
        int timeout = (tm == null) ? 0 : Integer.parseInt(tm);
        try {
            stub = new FachadaWSSGSSoapBindingStub(new URL(service.getFachadaWSSGSAddress()), service);
            if (timeout > 0) stub.setTimeout(timeout);
        } catch (MalformedURLException e) {
            System.err.println(e);
        } catch (AxisFault e) {
            System.err.println(e);
        }
        try {
            inputFactory = XMLInputFactory.newInstance();
        } catch (FactoryConfigurationError e) {
            System.err.println(e);
        }
    }

    /** Pointer para instância única de objeto desta classe. */
    private static Aurea aurea;

    /**
   * Acessa instância única de objeto desta classe.
   *
   * @return Instância única de objeto desta classe.
  */
    public static Aurea getInstance() {
        return (aurea == null) ? aurea = new Aurea() : aurea;
    }

    /** Representação de componente de séries temporais. */
    public class VO {

        /** Data da observação. */
        protected Date date;

        /** Valor numérico de alta precisão para o mês desta data. */
        protected BigDecimal value;

        /** Construtor default de objeto desta classe. */
        @SuppressWarnings("deprecation")
        public VO() {
            date = new Date(0, 0, 0, 0, 0, 0);
        }

        /**
     * Construtor de objeto desta classe com inicialização num instante específico.
     *
     * @param ano Ano da data da observação.
     * @param mes Mês da data da observação.
     * @param dia Dia da data da observação.
    */
        @SuppressWarnings("deprecation")
        public VO(int ano, int mes, int dia) {
            date = new Date(ano, mes, dia, 0, 0, 0);
        }

        /**
     * Acessa a data da observação.
     *
     * @return Date contendo a data da observação.
    */
        public Date getDate() {
            return date;
        }

        /**
     * Acessa o valor numérico para o mês desta data.
     *
     * @return valor Bigdecimal correspondente à data observada.
    */
        public BigDecimal getValue() {
            return value;
        }

        /**
     * Assinala o valor numérico para o mês desta data.
     *
     * @param s Representação <b>String</b> do valor.
    */
        protected void setValue(String s) {
            value = new BigDecimal(s.replaceAll(",", "."));
        }
    }

    /** ExcludeFilter p/uso exclusivo no getter que requisita a última observação... */
    private ExcludeFilter filter1;

    /**
   * Requisita a última observação disponível de uma série temporal.
   *
   * @param code Representação String do código da série temporal no SGS do BCB.
   * @return Observação Aurea.VO singular da série temporal.
   * @throws Exception Qualquer excessão do Axis ou do XMLStreamReader.
  */
    @SuppressWarnings("deprecation")
    public VO get(String code) throws Exception {
        VO vo = null;
        try {
            String xmlResponse = stub.getUltimoValorXML(Long.parseLong(code));
            XMLStreamReader rd = inputFactory.createXMLStreamReader(new StringReader(xmlResponse));
            if (filter1 == null) filter1 = new ExcludeFilter(new String[] { "NOME", "CODIGO", "PERIODICIDADE", "UNIDADE" });
            XMLStreamReader reader = inputFactory.createFilteredReader(rd, filter1);
            vo = new VO();
            while (reader.hasNext()) if ((reader.next() == XMLStreamConstants.START_ELEMENT) && reader.getLocalName().equals("DIA")) break;
            int number = Integer.parseInt(reader.getElementText());
            vo.date.setDate(number);
            reader.nextTag();
            number = Integer.parseInt(reader.getElementText());
            vo.date.setMonth(number - 1);
            reader.nextTag();
            number = Integer.parseInt(reader.getElementText());
            vo.date.setYear(number - 1900);
            do reader.nextTag(); while (!reader.isStartElement());
            vo.setValue(reader.getElementText());
            reader.close();
        } catch (Exception e) {
            throw e;
        }
        return vo;
    }

    /** ExcludeFilter p/uso exclusivo no getter que requisita sub-série temporal... */
    private ExcludeFilter filter2;

    /**
   * Requisita uma sub-série temporal num intervalo.
   *
   * @param code Representação String do código da série temporal no SGS do BCB.
   * @param dataInicial Representação String da data inicial da sub-série.
   * @param dataFinal Representação String da data final da sub-série.
   * @return Array de objetos componentes da sub-série.
   * @throws Exception Qualquer excessão do Axis ou do parser SAX2.
  */
    @SuppressWarnings("deprecation")
    public Object[] get(String code, String dataInicial, String dataFinal) throws Exception {
        List<VO> vector = new ArrayList<VO>();
        try {
            String xmlResponse = stub.getValoresSeriesXML(new long[] { Long.parseLong(code) }, dataInicial, dataFinal);
            XMLStreamReader rd = inputFactory.createXMLStreamReader(new StringReader(xmlResponse));
            if (filter2 == null) filter2 = new ExcludeFilter(new String[] { "BLOQUEADO" });
            XMLStreamReader reader = inputFactory.createFilteredReader(rd, filter2);
            while (reader.hasNext()) if ((reader.next() == XMLStreamConstants.START_ELEMENT) && reader.getLocalName().equals("DATA")) {
                String[] fields = reader.getElementText().split("/");
                int j = fields.length - 1;
                VO vo = new VO(Integer.parseInt(fields[j]) - 1900, (--j < 0) ? 0 : Integer.parseInt(fields[j]) - 1, (--j < 0) ? 1 : Integer.parseInt(fields[j]));
                reader.nextTag();
                vo.setValue(reader.getElementText());
                vector.add(vo);
            }
            reader.close();
        } catch (Exception e) {
            throw e;
        }
        return vector.toArray();
    }

    /**
   * Consulta via linha de comando.
   *
   * @param args
  */
    public static void main(String[] args) {
        if (!(args.length == 3 && args[0].matches("\\d+") && args[1].matches("\\d+/\\d+/\\d{4}") && args[2].matches("\\d+/\\d+/\\d{4}")) && !(args.length == 1 && args[0].matches("\\d+"))) {
            System.out.format("%nUso: java -jar bcb.jar Codigo [dataInicial dataFinal]%n%nCodigo é o código numérico da série temporal.%nDatas no formato dd/mm/aaaa.%n%n");
            System.exit(1);
        }
        String code = args[0], di = null, df = null;
        if (args.length == 3) {
            di = args[1];
            df = args[2];
        }
        try {
            Aurea aurea = Aurea.getInstance();
            System.out.format("SERVICE TIME OUT = %d%n%n", aurea.stub.getTimeout());
            WSSerieVO wo = aurea.stub.getUltimoValorVO(Long.parseLong(code));
            System.out.format("Código: %d%nNome Abreviado: %s%nNome Completo: %s%nGestor: %s%nFonte: %s%nAviso: %s%nPublica: %B%nPeriodicidade: %s%nSigla da Periodicidade: %s%nUnidade Padrão: %s%nData 1ª Observação: %d/%d/%d%nData Última Observação: %d/%d/%d%n%n", wo.getOid(), wo.getNomeAbreviado(), wo.getNomeCompleto(), wo.getGestorProprietario(), wo.getFonte(), wo.getAviso(), wo.isPublica(), wo.getPeriodicidade(), wo.getPeriodicidadeSigla(), wo.getUnidadePadrao(), wo.getDiaInicio(), wo.getMesInicio(), wo.getAnoInicio(), wo.getDiaFim(), wo.getMesFim(), wo.getAnoFim());
            if (wo.getValores() != null) {
                System.out.print("[");
                for (WSValorSerieVO valor : wo.getValores()) {
                    System.out.format(" %s", valor.getSvalor());
                }
                System.out.format(" ]%n%n");
            }
            if (args.length == 3) {
                Aurea.VO vo = aurea.get(code);
                System.out.format("%1$td/%1$tb/%1$tY %2$ 6.4f%n%n", vo.getDate(), vo.getValue());
                Object[] array = aurea.get(code, di, df);
                for (Object object : array) {
                    vo = (Aurea.VO) object;
                    System.out.format("%1$td/%1$tb/%1$tY %2$ 6.4f%n", vo.getDate(), vo.getValue());
                }
            }
        } catch (Exception e) {
            if (e instanceof AxisFault) {
                String fault = ((AxisFault) e).getFaultString();
                fault = (fault.length() == 0) ? "Excessão Remota desconhecida." : fault.substring(fault.indexOf(":") + 2);
                System.out.format("Falha via Axis: %s%n", fault);
            } else {
                e.printStackTrace();
            }
        }
    }

    /** Filtro por exclusão de elementos para StAX Parser. */
    private class ExcludeFilter implements StreamFilter {

        /** Array ordenado em ordem crescente dos nomes de elementos a excluir. */
        private String[] tagNames;

        /**
     * Construtor da classe visando performance otimizada.
     *
     * @param tagNames Array contendo os nomes de elementos a excluir.
    */
        public ExcludeFilter(String[] tagNames) {
            Arrays.sort(this.tagNames = tagNames);
        }

        /**
     * Verifica exclusão do elemento sob o cursor no StAX Parser.
     *
     * @param rd StAX Parser de documento XML sujeito a exclusão de elementos.
     * @return Status de exclusão do elemento sob o cursor.
    */
        public boolean accept(XMLStreamReader rd) {
            return !rd.hasName() || (Arrays.binarySearch(tagNames, rd.getLocalName()) < 0);
        }
    }
}
