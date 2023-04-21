package com.hs.mail.imap.parser;

import java.io.StringReader;
import java.util.LinkedList;

/**
 * Parses IMAP command into a list of tokens.
 * 
 * @author Won Chul Doh
 * @since 12 Jan, 2010
 * 
 */
public class CommandParser extends AbstractImapCommandParser {

    public CommandParser(StringReader in) {
        super(in);
    }

    /******************************************
     * THE COMMAND GRAMMAR STARTS HERE        *
     ******************************************/
    private boolean append() {
        if (!kw("APPEND") || !sp() || !mailbox() || !sp()) return false;
        flag_list();
        sp();
        date_time();
        sp();
        return literal();
    }

    private boolean authenticate() {
        return kw("AUTHENTICATE") && sp() && auth_type();
    }

    private boolean auth_type() {
        return atom();
    }

    public LinkedList<Token> command() {
        if (tag() && sp() && (command_any() || command_auth() || command_nonauth() || command_select() || command_custom()) && crlf()) return tokens; else throw new ParseException(tokens, "Syntax error in command");
    }

    private boolean command_any() {
        return kw("CAPABILITY") || kw("LOGOUT") || kw("NOOP");
    }

    private boolean command_auth() {
        return append() || create() || deleteacl() || delete() || examine() || getacl() || getquotaroot() || getquota() || listrights() || list() || lsub() || myrights() || namespace() || rename() || select() || setacl() || setquota() || status() || subscribe() || unsubscribe();
    }

    private boolean command_nonauth() {
        return login() || authenticate() || kw("STARTTLS");
    }

    private boolean command_select() {
        return kw("CHECK") || kw("CLOSE") || kw("EXPUNGE") || copy() || fetch() || search() || sort() || store() || thread() || uid();
    }

    private boolean command_custom() {
        return xrevoke();
    }

    private boolean copy() {
        return kw("COPY") && sp() && sequence_set() && sp() && mailbox();
    }

    private boolean create() {
        return kw("CREATE") && sp() && mailbox();
    }

    private boolean delete() {
        return kw("DELETE") && sp() && mailbox();
    }

    private boolean deleteacl() {
        return kw("DELETEACL") && sp() && mailbox() && sp() && userid();
    }

    private boolean examine() {
        return kw("EXAMINE") && sp() && mailbox();
    }

    private boolean fetch() {
        if (!kw("FETCH") || !sp() || !sequence_set() || !sp()) return false;
        if (kw("ALL") || kw("FULL") || kw("FAST") || fetch_att()) return true;
        if (!lparen()) return false;
        while (!rparen()) {
            sp();
            if (!fetch_att()) return false;
        }
        return true;
    }

    private boolean fetch_att() {
        if (kw("ENVELOPE") || kw("FLAGS") || kw("INTERNALDATE") || kw("RFC822.HEADER") || kw("RFC822.SIZE") || kw("RFC822.TEXT") || kw("RFC822") || kw("BODYSTRUCTURE") || kw("UID")) {
            return true;
        }
        if (kw("BODY.PEEK") || kw("BODY")) {
            if (!section()) {
                return false;
            } else {
                fetch_att_part();
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean fetch_att_part() {
        return kw("<") && number() && kw(".") && nz_number() && kw(">");
    }

    private boolean flag() {
        return kw("\\Answered") || kw("\\Flagged") || kw("\\Deleted") || kw("\\Seen") || kw("\\Draft") || flag_keyword() || flag_extension();
    }

    private boolean flag_extension() {
        return kw("\\") && atom();
    }

    private boolean flag_keyword() {
        return atom();
    }

    private boolean flag_list() {
        if (!lparen()) return false;
        while (!rparen()) {
            sp();
            if (!flag()) return false;
        }
        return true;
    }

    private boolean getacl() {
        return kw("GETACL") && sp() && mailbox();
    }

    private boolean getquota() {
        return kw("GETQUOTA") && sp() && astring();
    }

    private boolean getquotaroot() {
        return kw("GETQUOTAROOT") && sp() && mailbox();
    }

    private boolean header_fld_name() {
        return kw("FROM") || kw("TO") || kw("CC") || kw("BCC") || kw("SUBJECT") || astring();
    }

    private boolean header_list() {
        if (!lparen()) return false;
        while (!rparen()) {
            sp();
            if (!header_fld_name()) return false;
        }
        return true;
    }

    private boolean list() {
        return kw("LIST") && sp() && mailbox() && sp() && list_mailbox();
    }

    private boolean list_mailbox() {
        if (list_char(read())) {
            while (list_char(read())) ;
            unread();
            newToken(Token.Type.LIST_MAILBOX);
            return true;
        } else {
            unread();
            return string();
        }
    }

    private boolean listrights() {
        return kw("LISTRIGHTS") && sp() && mailbox() && sp() && userid();
    }

    private boolean login() {
        return kw("LOGIN") && sp() && userid() && sp() && password();
    }

    private boolean lsub() {
        return kw("LSUB") && sp() && mailbox() && sp() && list_mailbox();
    }

    private boolean mailbox() {
        return kw("INBOX") || astring();
    }

    private boolean myrights() {
        return kw("MYRIGHTS") && sp() && mailbox();
    }

    private boolean namespace() {
        return kw("NAMESPACE");
    }

    private boolean password() {
        return astring();
    }

    private boolean rename() {
        return kw("RENAME") && sp() && mailbox() && sp() && mailbox();
    }

    private boolean search() {
        if (!kw("SEARCH") || !sp()) return false;
        if (kw("CHARSET") && (!sp() || !astring() || !sp())) return false;
        do if (!search_key()) return false; while (sp());
        return true;
    }

    private boolean search_key() {
        if (kw("ALL") || kw("ANSWERED") || kw("BCC") && sp() && astring() || kw("BEFORE") && sp() && date() || kw("BODY") && sp() && astring() || kw("CC") && sp() && astring() || kw("DELETED") || kw("FLAGGED") || kw("FROM") && sp() && astring() || kw("KEYWORD") && sp() && flag_keyword() || kw("NEW") || kw("OLD") || kw("ON") && sp() && date() || kw("RECENT") || kw("SEEN") || kw("SINCE") && sp() && date() || kw("SUBJECT") && sp() && astring() || kw("TEXT") && sp() && astring() || kw("TO") && sp() && astring() || kw("UNANSWERED") || kw("UNDELETED") || kw("UNFLAGGED") || kw("UNKEYWORD") && sp() && flag_keyword() || kw("UNSEEN") || kw("DRAFT") || kw("HEADER") && sp() && header_fld_name() && sp() && astring() || kw("LARGER") && sp() && number() || kw("NOT") && sp() && search_key() || kw("OR") && sp() && search_key() && sp() && search_key() || kw("SENTBEFORE") && sp() && date() || kw("SENTON") && sp() && date() || kw("SENTSINCE") && sp() && date() || kw("SMALLER") && sp() && number() || kw("UID") && sp() && sequence_set() || kw("UNDRAFT") || sequence_set()) return true;
        if (!lparen()) return false;
        for (; !rparen(); sp()) if (!search_key()) return false;
        return true;
    }

    private boolean section() {
        if (!kw("[")) return false;
        section_spec();
        return kw("]");
    }

    private boolean section_msgtext() {
        return (kw("HEADER.FIELDS.NOT") || kw("HEADER.FIELDS")) && sp() && header_list() || kw("HEADER") || kw("TEXT");
    }

    private boolean section_part() {
        if (!number()) return false;
        for (; kw("."); number()) ;
        return true;
    }

    private boolean section_spec() {
        if (section_msgtext()) return true;
        if (section_part()) {
            section_text();
            return true;
        } else return false;
    }

    private boolean section_text() {
        return section_msgtext() || kw("MIME");
    }

    private boolean select() {
        return kw("SELECT") && sp() && mailbox();
    }

    private boolean setacl() {
        return kw("SETACL") && sp() && mailbox() && sp() && userid() && sp() && astring();
    }

    private boolean setquota() {
        return kw("SETQUOTA") && sp() && astring() && sp() && setquota_list();
    }

    private boolean setquota_list() {
        if (!lparen()) return false;
        while (!rparen()) {
            if (!setquota_resource()) return false;
        }
        return true;
    }

    private boolean setquota_resource() {
        return atom() && sp() && number();
    }

    private boolean sort() {
        if (!kw("SORT") || !sp() || !sort_criteria() || !kw("CHARSET") || !sp()) return false;
        do if (!search_key()) return false; while (sp());
        return true;
    }

    private boolean sort_criteria() {
        if (!lparen()) return false;
        for (; !rparen(); sp()) if (!sort_criterion()) return false;
        return true;
    }

    private boolean sort_criterion() {
        if (kw("REVERSE") && sp()) return sort_key(); else return sort_key();
    }

    private boolean sort_key() {
        return (kw("ARRIVAL") || kw("CC") || kw("DATE") || kw("FROM") || kw("SIZE") || kw("SUBJECT") || kw("TO"));
    }

    private boolean status() {
        if (!kw("STATUS") || !sp() || !mailbox() || !sp()) return false;
        if (!lparen()) return false;
        while (!rparen()) {
            sp();
            if (!status_att()) return false;
        }
        return true;
    }

    private boolean status_att() {
        return kw("MESSAGES") || kw("RECENT") || kw("UIDNEXT") || kw("UIDVALIDITY") || kw("UNSEEN");
    }

    private boolean store() {
        return kw("STORE") && sp() && sequence_set() && sp() && store_att_flags();
    }

    private boolean store_att_flags() {
        if (!kw("+FLAGS.SILENT") && !kw("-FLAGS.SILENT") && !kw("+FLAGS") && !kw("-FLAGS") && !kw("FLAGS.SILENT") && !kw("FLAGS") || !sp()) return false;
        if (flag_list()) return true;
        do if (!flag()) return false; while (sp());
        return true;
    }

    private boolean subscribe() {
        return kw("SUBSCRIBE") && sp() && mailbox();
    }

    private boolean tag() {
        if (tag_char(read())) {
            while (tag_char(read())) ;
            unread();
            newToken(Token.Type.TAG);
            return true;
        } else {
            unread();
            return false;
        }
    }

    private boolean thread() {
        if (!kw("THREAD") || !sp() || !thread_alg() || !sp()) return false;
        do if (!search_key()) return false; while (sp());
        return true;
    }

    private boolean thread_alg() {
        return kw("ORDEREDSUBJECT") || kw("REFERENCES") || atom();
    }

    private boolean uid() {
        return kw("UID") && sp() && (copy() || fetch() || search() || sort() || store() || thread());
    }

    private boolean unsubscribe() {
        return kw("UNSUBSCRIBE") && sp() && mailbox();
    }

    private boolean userid() {
        return astring();
    }

    private boolean xrevoke() {
        if (!kw("XREVOKE") || !sp() || !sequence_set()) return false;
        if (!sp() || !kw("FROM")) return true;
        if (sp()) do if (!userid()) return false; while (kw(","));
        return true;
    }
}
