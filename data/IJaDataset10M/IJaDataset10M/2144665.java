package br.org.acessobrasil.processoacessibilidade.bo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import br.org.acessobrasil.ases.regras.RegrasHardCodedEmag;
import br.org.acessobrasil.processoacessibilidade.vo.CssConteudoItemPro;
import br.org.acessobrasil.processoacessibilidade.vo.CssConteudoPro;

/**
 * Responsavel por retirar o codigo de css de dentro das tags
 * //TODO fazer baixar os arquivos de imagem dentro dos css inline
 * 
 * @author Fabio Issamu Oshiro
 *
 */
public class DesignerCss {

    private static RegrasHardCodedEmag regras = new RegrasHardCodedEmag();

    private static Logger logger = Logger.getLogger(DesignerCss.class);

    /**
	 * Retira o css inline
	 * @param codHtml
	 * @return codHtml sem css inline
	 */
    public String retirarCssInline(String codHtml, String pagina) {
        codHtml = codHtml.replace("<STYLE>", "<style type=\"text/css\">");
        codHtml = codHtml.replace("<STYLE type=text/css>", "<style type=\"text/css\">");
        codHtml = codHtml.replace("</STYLE>", "</style>");
        int id = 0;
        CssConteudoPro css = new CssConteudoPro();
        css.setListCssItem(new ArrayList<CssConteudoItemPro>());
        String retorno = codHtml;
        Pattern patTag = Pattern.compile("<[^>]*>");
        Matcher matTag = patTag.matcher(codHtml);
        while (matTag.find()) {
            String tagVelha = matTag.group();
            String tagNova = tagVelha;
            String style = regras.getAtributo(tagVelha, "style");
            String tagClass = regras.getAtributo(tagVelha, "class");
            String tagId = regras.getAtributo(tagVelha, "id");
            String width = regras.getAtributo(tagVelha, "width");
            String height = regras.getAtributo(tagVelha, "height");
            String border = regras.getAtributo(tagVelha, "border");
            String align = regras.getAtributo(tagVelha, "align");
            String bgcolor = regras.getAtributo(tagVelha, "bgcolor");
            if (style == null) style = "";
            if (!tagNova.startsWith("<object") && !tagNova.startsWith("<img")) {
                if (width != null && !width.equals("")) {
                    if (style.trim().endsWith(";") || style.equals("")) {
                        style += "width:" + width;
                    } else {
                        style += ";width:" + width;
                    }
                }
                if (height != null && !height.equals("")) {
                    if (style.trim().endsWith(";") || style.equals("")) {
                        style += "height:" + height;
                    } else {
                        style += ";height:" + height;
                    }
                }
                tagNova = tagNova.replaceAll("\\swidth=[^ >]*", "");
                tagNova = tagNova.replaceAll("\\sheight=[^ >]*", "");
            }
            if (tagNova.startsWith("<img") && border != null && !border.equals("")) {
                if (style.trim().endsWith(";") || style.equals("")) {
                    style += "border:" + border;
                } else {
                    style += ";border:" + border;
                }
                tagNova = tagNova.replaceAll("\\sborder=[^ >]*", "");
            }
            if (tagNova.startsWith("<p ") && align != null && !align.equals("")) {
                if (style.trim().endsWith(";") || style.equals("")) {
                    style += "text-align:" + align;
                } else {
                    style += ";text-align:" + align;
                }
                tagNova = tagNova.replaceAll("\\salign=[^ >]*", "");
            }
            if (tagNova.startsWith("<div ") && align != null && !align.equals("")) {
                if (style.trim().endsWith(";") || style.equals("")) {
                    style += "text-align:" + align;
                } else {
                    style += ";text-align:" + align;
                }
                tagNova = tagNova.replaceAll("\\salign=[^ >]*", "");
            }
            if (bgcolor != null && !bgcolor.equals("")) {
                if (style.trim().endsWith(";") || style.equals("")) {
                    style += "background-color:" + bgcolor;
                } else {
                    style += ";background-color:" + bgcolor;
                }
                tagNova = tagNova.replaceAll("\\sbgcolor=[^ >]*", "");
            }
            if (!style.equals("")) {
                String endTag = "";
                CssConteudoItemPro cssCriado = criar(style);
                CssConteudoItemPro igual = find(css, cssCriado);
                if (igual != null) {
                    cssCriado = igual;
                } else {
                    cssCriado.setNome("CMSA" + id++);
                }
                tagNova = tagNova.replaceAll("\\sstyle=\"[^\"]*\"", "");
                tagNova = tagNova.replaceAll("\\sstyle='[^']*'", "");
                if (tagNova.endsWith("/>")) {
                    tagNova = tagNova.substring(0, tagNova.length() - 2);
                    endTag = "/>";
                } else {
                    tagNova = tagNova.substring(0, tagNova.length() - 1);
                    endTag = ">";
                }
                if (tagClass.equals("")) {
                    tagNova = tagNova + " class=\"" + cssCriado.getNome() + "\"";
                } else {
                    tagNova = tagNova.replace("class=\"" + tagClass + "\"", "class=\"" + tagClass + " " + cssCriado.getNome() + "\"");
                    tagNova = tagNova.replace("class='" + tagClass + "'", "class=\"" + tagClass + " " + cssCriado.getNome() + "\"");
                    tagNova = tagNova.replace(" class=" + tagClass + " ", " class=\"" + tagClass + " " + cssCriado.getNome() + "\" ");
                }
                tagNova += " " + endTag;
                tagNova = tagNova.replaceAll("\\s+", " ");
                logger.debug("Trocado em " + pagina + ":\n" + tagVelha.replaceAll("\\s+", " ") + "\n por \n" + tagNova + "\n\n");
                retorno = retorno.replace(tagVelha, tagNova);
                if (!tagId.equals("")) {
                    if (cssCriado.getListTagId() == null) {
                        cssCriado.setListTagId(new ArrayList<String>());
                    }
                    cssCriado.getListTagId().add(tagId);
                }
                if (!css.getListCssItem().contains(cssCriado)) {
                    css.getListCssItem().add(cssCriado);
                }
            }
        }
        String conteudoCss = css.toString();
        String cssBlock = "<style type=\"text/css\" title=\"DesignerCss\">\n" + conteudoCss + "</style>\n";
        if (conteudoCss.trim().equals("")) {
            cssBlock = "";
        }
        if (retorno.indexOf("</head>") != -1) {
            retorno = retorno.replace("</head>", cssBlock + "</head>");
        } else {
            if (retorno.indexOf("</HEAD>") != -1) {
                retorno = retorno.replace("</HEAD>", cssBlock + "</head>");
            } else {
                retorno = retorno + cssBlock;
            }
        }
        return retorno;
    }

    /**
	 * Cria o obj com a string informada
	 * @param style
	 * @return obj CssConteudoItemPro
	 */
    private CssConteudoItemPro criar(String style) {
        CssConteudoItemPro cssItem = new CssConteudoItemPro();
        ArrayList<String> valores = new ArrayList<String>();
        String listAtributos[] = style.split(";");
        for (int i = 0; i < listAtributos.length; i++) {
            String filtro = listAtributos[i].replaceAll("\\s+", " ").trim();
            filtro = arrumarLetrasMaiusculas(filtro);
            valores.add(filtro);
        }
        cssItem.setAtributoValor(valores);
        return cssItem;
    }

    private String arrumarLetrasMaiusculas(String atributo) {
        atributo = atributo.replace("WIDTH:", "width:");
        atributo = atributo.replace("LEFT:", "left:");
        atributo = atributo.replace("TOP:", "top:");
        atributo = atributo.replace("HEIGHT:", "height:");
        return atributo;
    }

    /**
	 * Procura por um item com valores iguais
	 * @param cssItem
	 * @return o item
	 */
    private CssConteudoItemPro find(CssConteudoPro css, CssConteudoItemPro cssItem) {
        for (CssConteudoItemPro cssItemA : css.getListCssItem()) {
            if (cssItemA.getAtributoValor().size() != cssItem.getAtributoValor().size()) continue;
            boolean igual = true;
            for (int j = 0; j < cssItemA.getAtributoValor().size(); j++) {
                if (!cssItem.getAtributoValor().get(j).equals(cssItemA.getAtributoValor().get(j))) {
                    igual = false;
                    break;
                }
            }
            if (igual) return cssItemA;
        }
        return null;
    }
}
