package mv.wap;

/**
 * User: Gantz
 * Date: Nov 5, 2009
 * Time: 11:59:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Section extends Element {

    private String _header;

    private int _level;

    public Section(String header, Element... children) {
        super(children);
        _header = header;
    }

    @Override
    String toHtml(Element parent) {
        _level = parent.get(0, "Level") + 1;
        final String header = new StringBuffer().append("<h").append(_level).append(">").append(_header).append("</h").append(_level).append(">").toString();
        return build(new BlockRunner() {

            public void append(StringBuffer br) {
                br.append(header);
                for (Element c : getChildren()) {
                    br.append(c.toHtml(Section.this));
                }
            }
        });
    }

    public int getLevel() {
        return _level;
    }
}
