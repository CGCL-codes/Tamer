package br.org.skenp.writers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import br.org.skenp.Reader;
import br.org.skenp.metadata.Column;
import br.org.skenp.metadata.ColumnType;
import br.org.skenp.metadata.ConcretColumn;
import br.org.skenp.metadata.Table;
import br.org.skenp.util.NamesUtil;

/**
 * @author Davy Diegues Duran
 *
 */
public class EntityWriter extends Writer {

    private static final String TEMPLATE_FILE = "./conf/EntityTemplate.java";

    private String _package;

    /**
	 * @param fileName
	 * @throws IOException
	 */
    public EntityWriter(String fileName) throws IOException {
        this(new File(fileName));
    }

    /**
	 * @param file
	 * @throws IOException
	 */
    public EntityWriter(File file) throws IOException {
        super(NamesUtil.packageToFileName(file.getAbsolutePath()));
        _package = NamesUtil.getPackageName(file.getAbsolutePath());
    }

    @Override
    public void writeComponentFile(Object table) throws Exception {
        Table t = (Table) table;
        Reader r = new Reader(TEMPLATE_FILE);
        String template = r.readAll();
        template = template.replace("[package]", _package);
        template = template.replace("[tableName]", t.getName());
        write(template.substring(0, template.indexOf("[domainClassName]")));
        template = template.substring(template.indexOf("[domainClassName]"));
        String domainClassName = NamesUtil.toClassName(t.getName());
        template = template.replace("[domainClassName]", domainClassName);
        write(template.substring(0, template.indexOf("[attributes]")));
        template = template.substring(template.indexOf("[attributes]"));
        template = template.replace("[attributes]", getAllAttributesDeclaration(t.getColumns()));
        write(template.substring(0, template.indexOf("[gettersMethods]")));
        template = template.substring(template.indexOf("[gettersMethods]"));
        template = template.replace("[gettersMethods]", TransferObjectWriter.getAllGettersMethods(t.getColumns()));
        write(template.substring(0, template.indexOf("[settersMethods]")));
        template = template.substring(template.indexOf("[settersMethods]"));
        template = template.replace("[settersMethods]", TransferObjectWriter.getAllSettersMethods(t.getColumns()));
        write(template.substring(0, template.indexOf("[toStringContent]")));
        template = template.substring(template.indexOf("[toStringContent]"));
        template = template.replace("[toStringContent]", TransferObjectWriter.getToStringMethodContent(t.getColumns()));
        writeln(template);
        endWrite();
    }

    private String getAllAttributesDeclaration(Collection<Column> columns) {
        StringBuffer attributes = new StringBuffer();
        for (Column c : columns) {
            if (c.getColumnType() == ColumnType.CONCRET) {
                ConcretColumn cc = (ConcretColumn) c;
                if (cc.isAPrimaryKey()) attributes.append("\n\t@Id");
                attributes.append("\n\t@Column(name=" + cc.getName());
                attributes.append(", null=" + cc.isNullable());
                attributes.append(")");
            } else if (c.getColumnType() == ColumnType.IMPORTED) {
                attributes.append("\n\t@ManyToOne");
            }
            attributes.append("\n\tprivate " + TransferObjectWriter.getAttibuteDeclaration(c) + ";");
        }
        return attributes.toString();
    }
}
