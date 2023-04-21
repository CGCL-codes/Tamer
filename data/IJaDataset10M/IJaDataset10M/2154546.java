package gatchan.phpparser.parser;

import junit.framework.Assert;
import junit.framework.TestCase;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The PHP Parser test case.
 *
 * @author Matthieu Casanova
 * @version $Id: PHPParserTester.java 20113 2011-10-18 17:21:33Z kpouer $
 */
public class PHPParserTester extends TestCase implements PHPParserListener {

    private PHPParser phpParser;

    public void testNew() {
    }

    public void testParserSuccess() {
        checkPHP("try {\n" + "}\n" + "catch (Exception $e) {\n" + "}");
        checkPHP("$a = \"\\n\\n\";");
        checkPHP("use N\\ett\\e;");
        checkPHP("use Nette;");
        checkPHP("use \\Ne\\tte;");
        checkPHP("namespace Nette\\Application {\n" + "use Nette;\n" + "// ...\n" + "}");
        checkPHP("function objclone() { return clone($z); }");
        checkPHP("function objclone() { return clone $z; }");
        checkHTML("<? toto();?>");
        checkHTML("<? toto($this->highlightfile->linkscripts{$category});?>");
        checkHTML("<? while (true) : ?>coucou<? endwhile;?>");
        checkHTML("<? if (true) : ?>coucou<? endif;?>");
        checkHTML("<? if (true) : ?>coucou<? elseif (true) :?>coucou<? elseif (true) :?>coucou<? else :?>coucou<? endif;?>");
        checkHTML("<? while (true) : ?>coucou<? endwhile;?>");
        checkHTML("<? foreach ($a as $b) : ?>coucou<? endforeach;?>");
        checkHTML("<? for (;;) : ?>coucou<? endfor;?>");
        checkPHP("fn()->toto;");
        checkPHP("$a = float;");
        checkPHP("$link= mysql_connect($this->mysqlHost, $this->mysqlUser, $this->mysqlPassword)\n" + "or $errMsg= 'Could not connect: ' . mysql_error();");
        checkPHP("function method(array $array) {\n}");
        checkPHP("if (true or $b = 2) echo 'coucou';");
        checkPHP("!feof($fin) && $data = fread($fin, 8096);");
        checkPHP("if ($foo = bar()) echo 'coucou';");
        checkPHP("echo \"foo$\";");
        checkPHP("echo \"$1\";");
        checkPHP("echo \"$\";");
        checkPHP("echo \"$ foo\";");
        checkPHP("$b[1];");
        checkPHP("$b[1]->test;");
        checkPHP("$b[1]->test();");
        checkPHP("$b[1]->test[1];");
        checkPHP("$b[1]->test[1]->tata();");
        checkPHP("array('a' => float);");
        checkPHP("$a = @require 'b';");
        checkPHP("@list($sFormatted, $sExt) = explode(' ', $sFormatted, 2);");
        checkPHP("$tpl->define(array());");
        checkPHP("if ($a && !empty($c)) {echo 'coucou';}");
        checkPHP("class test { function &fetchRow($result, $fetchmode = DB_FETCHMODE_DEFAULT, $rownum=null) \n{ \n } \n }");
        checkPHP("($plant_loc == \"KV\") ? PRINT \"<TH>OEE %</TH>\" : PRINT \"<TH>UEE %</TH>\";");
        checkPHP("$a == 0 ? print \"true\" : print \"false\";");
        checkPHP("if(!$result = mysql_query($sql)) return(array());");
        checkPHP("!list($a) = $b;");
        checkPHP("list ($catid) = sql_fetch_row($result, $dbi);");
        checkPHP("1 == 1 ? $twovar = $onevar: $twovar = $threevar;");
        checkPHP("list($a,$b);");
        checkHTML("<?php\n" + "$heredoc = <<<EOF\n" + "?>\n" + "EOF;\n" + "?>");
        checkPHP("if ($some xor $thing) { }");
        checkHTML("<?=\"toto\"?>");
        checkPHP("$ a = <<<ca\n" + "\n" + "toto\n" + "\n" + "ca;");
        checkPHP("$ebus_sql['sel_url_list'] = <<<EOS\n" + "select rtrim(URL_NAME) as url_name\n" + "	, rtrim(URL) as url\n" + "	, rtrim(URL_DESC) as url_desc\n" + "from appl_url\n" + "where appl_instnc_sk = <<INSTNC>>\n" + "and appl_sect_deftn_sk = <<SECT>>\n" + "order by url_ord\n" + "EOS;\n");
        checkHTML("<?php echo $bgcolor2?>");
        checkPHP("if ($topic<1) { $topic = 1;}");
        checkPHP("$this->result_field_names[$result_id][] = odbc_field_name($result_id, $i);");
        checkPHP("$db->sql_query($sql);");
        checkPHP("$val = $$add;");
        checkPHP("foreach ($HTTP_GET_VARS as $secvalue) { }");
        checkPHP("\"\\\"\";");
        checkPHP("$v->read();");
        checkPHP("$add = 'a'.$i;$val = $$add;");
        checkPHP("($a==\"b\") || (c($this->x)==\"d\");");
        checkPHP("(substr($this->file, 0, 2) == \"MM\");");
        checkPHP("(substr($this->file, 0, 2) == \"MM\") || substr($this->file, 0, 2) == \"II\";");
        checkPHP("return (substr($this->file, 0, 2) == \"MM\") || substr($this->file, 0, 2) == \"II\";");
        checkPHP("$this->highlightfile->linkscripts{$category};");
        checkPHP("$code = call_user_method($this->highlightfile->linkscripts{$category}, $this->highlightfile, $oldword, $this->output_module);");
        checkPHP("$this->startmap[$startcurrtag]();");
        checkPHP("new $this->startmap[$startcurrtag]();");
        checkPHP("$this->highlightfile = new $this->startmap[$startcurrtag]();");
        checkPHP("echo \"Test\", \"me\";");
        checkPHP("print (\"Test me\");");
        checkPHP("$s = <<<HEREDOC \n dskjfhskj\n \n\nHEREDOC;");
        checkPHP("call_user_method_array($function_name[1], ${$objectname}, $arguments);");
        checkPHP("$connect_function($dbhost, $user, $pw);");
        checkPHP("@$connect_function($dbhost, $user, $pw);");
        checkPHP("$conn = @$connect_function($dbhost, $user, $pw);");
        checkPHP("global ${$objectname}; ");
        checkPHP("class DB_mssql extends DB_common { var $connection; var $phptype, $dbsyntax; }  ");
        checkPHP("unset($this->blockvariables[$block][$varname]);");
        checkPHP("new IT_Error(\"The block '$block' was not found in the template.\", __FILE__, __LINE__);");
        checkPHP("for ($i=156, $j=0; $i<512; $i++, $j++) $v_checksum += ord(substr($v_binary_data_last,$j,1));");
        checkPHP("static $last_run = 0;");
        checkPHP("unset($headers['Subject']);");
        checkPHP("switch($func) {\n case \"f0\":\n case \"f1\":\n f1();\n break; \n case \"tt\": \n default: \n f0(); \n break;\n }");
        checkPHP("function validateAndParseResponse($code, &$arguments) { }");
        checkPHP("$options = Console_Getopt::getopt($argv, \"h?v:e:p:d:\");");
        checkPHP("$this->container = new $container_class($container_options);");
        checkPHP("class Cmd extends PEAR { var $arrSetting     = array(); }");
        checkPHP("class Cmd extends PEAR { var $arrSetting     = array(), $i=10; }");
        checkPHP("if (isset($test)) { } elseif (isset($lang)) { }");
        checkPHP("require_once(\"mainfile.php\");  ");
        checkPHP("if (eregi(\"footer.php\",$PHP_SELF)) {\n" + "Header(\"Location: index.php\");\n" + "die();\n" + "}\n");
        checkPHP("while (eregi(\"footer.php\",$PHP_SELF)) {\n" + "Header(\"Location: index.php\");\n" + "die();\n" + "}\n");
        checkPHP("while (eregi(\"footer.php\",$PHP_SELF)) :\n" + "Header(\"Location: index.php\");\n" + "die();\n" + "endwhile;\n");
        checkPHP("$tipath = \"images/topics/\";");
        checkPHP("$reasons = array(\"1\", \"2\",\"test\");");
        checkPHP("if ($home == 1) { message_box(); blocks(Center);}");
        checkPHP("$bresult = sql_query(\"select * from \".$prefix.\"_banner WHERE type='0' AND active='1'\", $dbi);");
        checkPHP("switch($func) {\n case \"f1\":\n f1();\n break; \n default: \n f0(); \n break;\n }");
        checkPHP("if (!$name) { \n }");
        checkPHP("mt_srand((double)microtime()*1000000);");
        checkPHP("$alttext = ereg_replace(\"\\\"\", \"\", $alttext);");
        checkPHP("$message .= \"\"._THISISAUTOMATED.\"\\n\\n\";");
        checkPHP("if (!empty($pass) AND $pass==$passwd) { }");
        checkPHP("$AllowableHTML = array(\"b\"=>1,\n \"i\"=>1);");
        checkPHP("if ($term{0}!=$firstChar) {}");
        checkPHP("echo \"<center><b>\"._NOADMINYET.\"</b></center><br><br>\"\n" + ".\"<form action=\\\"admin.php\\\" method=\\\"post\\\">\"\n" + ".\"<tr><td><b>\"._NICKNAME.\":</b></td><td><input type=\\\"text\\\" name=\\\"name\\\" size=\\\"30\\\" maxlength=\\\"25\\\"></td></tr>\"\n;");
        checkPHP("/* \n overLib is from Eric Bosrup (http://www.bosrup.com/web/overlib/) \n */");
        checkPHP("if ($arrAtchCookie[1]==0 && $IdAtchPostId!=null){  } ");
        checkPHP("$arrAtchCookie[1] -= filesize(realpath($AtchTempDir).\"/\".$xattachlist)/ 1024; ");
        checkPHP("if (!isset($message)){ \n" + "$message = $myrow[post_text];\n" + "$message = eregi_replace(\"\\[addsig]\", \"\\n-----------------\\n\" .    $myrow[user_sig], $message); \n" + "$message = str_replace(\"<BR>\", \"\\n\", $message); \n" + "$message = str_replace(\"<br>\", \"\\n\", $message); \n } ");
        checkPHP("do {$array[] = array(\"$myrow[uid]\" => \"$myrow[uname]\"); } while($myrow = mysql_fetch_array($result));");
        checkPHP("$ol = new Overlib();");
        checkPHP("$risultato = mysql_query($sql) or\n    die(mysql_error());");
        checkHTML("\n\n\n\n  <?php print \"Hello world\" ?>");
        checkHTML("<?php phpinfo(); ?>");
        checkHTML("<?php phpinfo()?>");
        checkHTML("<?php phpinfo(); ?> foo <?php phpinfo(); ?>");
        checkHTML(" <?php //this is a line comment ?>");
        checkHTML("<?php echo $module_name ?>");
        checkHTML("<?php $x = function() { echo 'hello'; }; ?>");
        checkPHP("$foo = \"{$_POST}\";");
        checkPHP("$foo = \"$_POST\";");
        checkPHP("$foo = \"$_POST['some name']\";");
        checkPHP("$foo = \"{$_POST['some name']}\";");
        checkPHP("TR_TreeAction::getInstance('containers')->isRoot(5);");
    }

    public void testParserSinglePHP5() {
        checkPHP("interface Test { function tutu(); }");
    }

    public void testParserSuccessPHP5SpecialSyntax() {
        checkPHP("TR_TreeAction::getInstance('containers');");
        checkPHP("TR_TreeAction::getInstance('containers')->isRoot(5);");
        checkPHP("function method(array $array) {\n}");
        checkPHP("abstract class Test {}");
        checkPHP("abstract class Test { var $toto,$tata;}");
        checkPHP("abstract class Test { $toto;}");
        checkPHP("abstract class Test { private $toto;}");
        checkPHP("abstract class Test { const toto = 3;}");
        checkPHP("abstract class Test { function tutu() {} }");
        checkPHP("abstract class Test { private function tutu() {} }");
        checkPHP("abstract class Test { abstract function tutu(); }");
        checkPHP("abstract class Test { abstract protected function tutu(); }");
        checkPHP("abstract class Test { protected abstract function tutu(); }");
        checkPHP("abstract class Test { final function tutu() {} }");
        checkPHP("abstract class Test { private final function tutu() {} }");
        checkPHP("abstract class Test { final private function tutu() {} }");
        checkPHP("interface Test {  function tutu(); }");
        checkPHP("interface Test {  const tata = 3; }");
        checkPHP("interface Test extends Tata { function tutu(); }");
        checkPHP("interface Test extends Tata, Toto { function tutu(); }");
        checkPHP("class Test extends Tata { protected static function tutu() {} }");
        checkPHP("class Test extends Tata { protected final static function tutu() {} }");
        checkPHP("class Test extends Tata { final protected static function tutu() {} }");
        checkPHP("class Test implements Toto {  }");
        checkPHP("class Test implements Toto, Tata {  }");
        checkPHP("function tutu(Toto $a) {  }");
        checkPHP("try { } catch(MyException $a) {}");
        checkPHP("throw new Toto();");
        checkPHP("$b[1]->test()->tutu();");
    }

    private void checkPHP(String s) {
        try {
            System.out.println(s);
            phpParser.phpParserTester(s);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
            System.out.println(s);
            e.printStackTrace();
        }
    }

    private void checkHTML(String s) {
        try {
            System.out.println(s);
            phpParser.htmlParserTester(s);
        } catch (ParseException e) {
            System.out.println(s);
            Assert.fail(e.getMessage());
        } catch (Error e) {
            System.out.println(s);
            Assert.fail(e.getMessage());
        }
    }

    private void checkHTML(final File strEval, final boolean good) {
        ParseException ex = null;
        try {
            System.out.println("strEval = " + strEval.toString());
            phpParser.htmlParserTester(strEval);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            ex = e;
        }
        if (good) {
            try {
                assertTrue(ex == null);
            } catch (RuntimeException e) {
                ex.printStackTrace();
                throw e;
            }
        } else {
            assertNotNull(ex);
        }
    }

    private void testDirectory(final File file) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                testDirectory(files[i]);
            }
        } else {
            if (file.getName().toUpperCase().endsWith(".PHP")) {
                checkHTML(file, true);
            }
        }
    }

    public void parseError(PHPParseErrorEvent e) {
        throw new Error(e.getMessage());
    }

    public void parseMessage(PHPParseMessageEvent e) {
    }

    protected void setUp() throws Exception {
        phpParser = new PHPParser();
        phpParser.addParserListener(this);
    }
}
